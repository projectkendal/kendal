package kendal.processor;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import kendal.api.AstHelper;
import kendal.api.KendalHandler;
import kendal.api.exceptions.KendalException;
import kendal.api.exceptions.KendalRuntimeException;
import kendal.api.impl.AstHelperImpl;
import kendal.api.inheritance.Inherit;
import kendal.model.ForestBuilder;
import kendal.model.Node;
import kendal.utils.ForestUtils;
import kendal.utils.KendalMessager;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.MirroredTypeException;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class KendalProcessor extends AbstractProcessor {

    private Context context;
    private Trees trees;
    private ForestBuilder forestBuilder;
    private KendalMessager messager;
    private AstHelper astHelper;
    private boolean firstRound;
    private TreeMaker treeMaker;
    private Set<Node> firstRoundNodes;
    private JavacProcessingEnvironment procEnv;


    @Override
    public void init(ProcessingEnvironment processingEnv) {
        procEnv = (JavacProcessingEnvironment) processingEnv;
        trees = Trees.instance(processingEnv);
        messager = new KendalMessager(processingEnv.getMessager());
        forestBuilder = new ForestBuilder(trees, messager);
        firstRound = true;
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        context = procEnv.getContext();
        astHelper = new AstHelperImpl(context);
        treeMaker = TreeMaker.instance(context);
        long startTime = System.currentTimeMillis();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processor run!");
        if (roundEnv.processingOver()) return false;
        Set<Node> forest = forestBuilder.buildForest(roundEnv.getRootElements());

        if(firstRound) {
            try {
                new AnnotationInheritanceHandler(astHelper, treeMaker).handleAnnotationInheritance(forest);
            } catch (KendalRuntimeException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
            try {
                forceNewProcessingRound();
                firstRoundNodes = forest;
                firstRound = false;
            } catch (IOException e) {
                throw new KendalRuntimeException(e.getMessage());
            }
        } else {
            if(firstRoundNodes != null) {
                // handle nodes found on the first round, which we could not handle then because of annotation inheritance processing
                forest.addAll(firstRoundNodes);
                firstRoundNodes = null;
            }
            Set<KendalHandler> handlers = getHandlersFromSPI();
            displayRegisteredHandlers(handlers);
            executeHandlers(getHandlerAnnotationsMap(handlers, forest), forest);
        }

        messager.printElapsedTime("Processor", startTime);
        return false;
    }

    private void forceNewProcessingRound() throws IOException {
        JavaFileObject fileObject = processingEnv.getFiler().createSourceFile("KendalGreatestFrameworkInTheWorld", null);
        Writer writer = fileObject.openWriter();
        writer.close();
    }

    private Map<KendalHandler, Set<Node>> getHandlerAnnotationsMap(Set<KendalHandler> handlers, Set<Node> forest) {
        long startTime = System.currentTimeMillis();
        Map<KendalHandler, Set<Node>> handlersWithNodes = handlers.stream()
                .collect(Collectors.toMap(Function.identity(), h -> new HashSet<>()));

        // annotation FQN -> Handler
        // Handler may be null, if annotation is not handled by any handler
        Map<String, KendalHandler> annotationToHandler = handlers.stream()
                .filter(kendalHandler -> kendalHandler.getHandledAnnotationType() != null)
                .collect(Collectors.toMap(h -> h.getHandledAnnotationType().getName(), Function.identity()));

        Map<String, Node<JCClassDecl>> annotationsDeclMap = ForestUtils.getAnnotationsDeclMap(forest);

        ForestUtils.traverse(forest, node -> {
            if(node.is(JCAnnotation.class)) {
                assignNodeToHandler(node, annotationToHandler, handlersWithNodes, annotationsDeclMap);
            }
        });

        messager.printElapsedTime("Annotations' scanner", startTime);
        return handlersWithNodes;
    }

    private void assignNodeToHandler(Node<JCAnnotation> node, Map<String, KendalHandler> annotationToHandler,
                                     Map<KendalHandler, Set<Node>> handlersWithNodes, Map<String, Node<JCClassDecl>> annotationsDeclMap) {
        String fqn = node.getObject().type.tsym.getQualifiedName().toString();
        if(annotationToHandler.containsKey(fqn)) {
            // handler for this annotation type is already cached. Possibly null
            KendalHandler handler = annotationToHandler.get(fqn);
            if(handler != null) {
                handlersWithNodes.get(handler).add(node);
            }
        } else {
            // try to inherit handler
            Inherit inherit = node.getObject().type.tsym.getAnnotation(Inherit.class);
            resolveInherit(fqn, inherit, annotationToHandler, annotationsDeclMap);
            KendalHandler handler = annotationToHandler.get(fqn);
            if(handler != null) {
                handlersWithNodes.get(handler).add(node);
            }
        }
    }

    private void resolveInherit(String fqn, Inherit inherit, Map<String, KendalHandler> annotationToHandler, Map<String, Node<JCClassDecl>> annotationsDeclMap) {
        if(inherit == null) {
            // we don't inherit, so insert null handler
            annotationToHandler.put(fqn, null);
            return;
        }
        try {
            inherit.inheritedAnnotationClass();
        } catch(MirroredTypeException e) {
            Type.ClassType inheritedAnnotationType = (Type.ClassType) e.getTypeMirror();
            String inheritedFqn = inheritedAnnotationType.tsym.getQualifiedName().toString();

            if(annotationToHandler.containsKey(inheritedFqn)) {
                annotationToHandler.put(fqn, annotationToHandler.get(inheritedFqn));
            } else {
                resolveInherit(inheritedFqn, inheritedAnnotationType.tsym.getAnnotation(Inherit.class), annotationToHandler, annotationsDeclMap);
                annotationToHandler.put(fqn, annotationToHandler.get(inheritedFqn));
            }
            return;
        }
        throw new KendalRuntimeException("Inherited annotation type mirror could not be resolved for " + fqn); // should never happen, but let's throw just in case
    }

    private Set<KendalHandler> getHandlersFromSPI() {
        return StreamSupport
                .stream(ServiceLoader.load(KendalHandler.class, KendalProcessor.class.getClassLoader()).spliterator(), false)
                .collect(Collectors.toSet());
    }

    private void displayRegisteredHandlers(Set<KendalHandler> handlers) {
        messager.printMessage(Diagnostic.Kind.NOTE, "### Handlers' registration ###");
        handlers.forEach(handler -> {
            String target = handler.getHandledAnnotationType() != null ? handler.getHandledAnnotationType().toString() : "all nodes";
            messager.printMessage(Diagnostic.Kind.NOTE,
                    String.format("%s registered as provider for %s", handler.getClass().getName(), target));
        });
    }

    private void executeHandlers(Map<KendalHandler, Set<Node>> handlersMap, Set<Node> forest) {
        messager.printMessage(Diagnostic.Kind.NOTE, "### Handlers' execution ###");
        handlersMap.forEach((handler, annotationNodes) -> {
            try {
                long startTime = System.currentTimeMillis();
                if (handler.getHandledAnnotationType() != null) handler.handle(annotationNodes, astHelper);
                else handler.handle(forest, astHelper);
                messager.printElapsedTime("Handler" + handler.getClass().getName(), startTime);
            } catch (KendalException | KendalRuntimeException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        });
    }
}