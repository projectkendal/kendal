package kendal.processor;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Symbol.ClassSymbol;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
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
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static kendal.utils.AnnotationUtils.*;

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
//        if (roundEnv.getRootElements().isEmpty()) return false;
        if (roundEnv.processingOver()) return false;
        Set<Node> forest = forestBuilder.buildForest(roundEnv.getRootElements());

        if(firstRound) {
            handleAnnotationInheritance(forest);
            try {
                JavaFileObject fileObject = processingEnv.getFiler().createSourceFile("KendalGreatestFrameworkInTheWorld.java", null);
                Writer writer = fileObject.openWriter();
                writer.close();
                firstRoundNodes = forest;
                firstRound = false;
            } catch (IOException e) {
                throw new KendalRuntimeException(e.getMessage());
            }
        } else {
            if(firstRoundNodes != null) {
                // handle nodes found on the first round, which we could not handle then because of annotation extending processing
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

    private void handleAnnotationInheritance(Set<Node> forest) {
        handleInherit(forest);
        handleAttribute(forest);
        handleAttrReference(forest);
    }

    private void handleInherit(Set<Node> forest) {
        Set<Node> inherits = getAnnotationsOfType(forest, Inherit.class);
        Set<JCClassDecl> handledAnnotations = new HashSet<>();
        inherits.forEach(node -> handleInheritingAnnotation((JCClassDecl) node.getParent().getObject(),
                handledAnnotations,
                getAnnotationsDeclMap(forest)));
    }

    private void handleInheritingAnnotation(JCClassDecl annotationDecl, Set<JCClassDecl> handledNodes, Map<String, Node<JCClassDecl>> annotationsDeclMap) {
        if(handledNodes.contains(annotationDecl)) {
            return;
        }
        JCAnnotation inheritAnnotation = getJCAnnotation(annotationDecl, Inherit.class);
        if(inheritAnnotation != null) {
            String inheritedAnnotationName = ((JCIdent) ((JCAnnotation) ((JCAssign) inheritAnnotation.args.head).rhs).annotationType).sym.getQualifiedName().toString();

            Map<String, JCTree> inheritedDefs = new HashMap<>();
            if(annotationsDeclMap.containsKey(inheritedAnnotationName)) {
                // inherited annotation is part of the compilation
                Node<JCClassDecl> inheritedAnnotation = annotationsDeclMap.get(inheritedAnnotationName);
                handleInheritingAnnotation(inheritedAnnotation.getObject(), handledNodes, annotationsDeclMap);
                inheritedDefs = inheritedAnnotation.getObject().defs.stream()
                        .collect(Collectors.toMap(h -> h.type.tsym.name.toString(), Function.identity()));
            } else {
                // inherited annotation has to be retrieved through ClassSymbol
                ClassSymbol inheritedAnnotationSymbol = (ClassSymbol) ((JCIdent) ((JCAnnotation) ((JCAssign) inheritAnnotation.args.head).rhs).annotationType).sym;
                for (Scope.Entry entry = inheritedAnnotationSymbol.members().elems; entry != null; entry = entry.sibling) {
                    if(entry.sym instanceof Symbol.MethodSymbol) {
                        JCMethodDecl methodDecl = treeMaker.MethodDef((Symbol.MethodSymbol) entry.sym, null);
                        Attribute defaultValue = ((Symbol.MethodSymbol) entry.sym).defaultValue;
                        methodDecl.defaultValue = defaultValue == null ? null : treeMaker.Literal(defaultValue.getValue());
                        inheritedDefs.put(entry.sym.name.toString(), methodDecl);
                    }
                }
            }

            // override default values
            for (JCExpression arg : ((JCAnnotation) ((JCAssign) inheritAnnotation.args.head).rhs).args) {
                JCTree def = inheritedDefs.get(((JCIdent) ((JCAssign) arg).lhs).name.toString());
                if(def instanceof JCMethodDecl) {
                    ((JCMethodDecl) def).defaultValue = ((JCAssign) arg).rhs;
                }
            }

            annotationDecl.defs = annotationDecl.defs.appendList(astHelper.getAstUtils().toJCList(new ArrayList<>(inheritedDefs.values())));
            inheritAnnotation.args = com.sun.tools.javac.util.List.nil();
            handledNodes.add(annotationDecl);
        }
    }

    private void handleAttribute(Set<Node> forest) {

    }

    private void handleAttrReference(Set<Node> forest) {

    }

    private JCAnnotation getJCAnnotation(JCClassDecl classDecl, Class<? extends Annotation> annotationClass) {
        return classDecl.mods.annotations.stream()
                .filter(ann -> ann.type.tsym.getQualifiedName().contentEquals(annotationClass.getName()))
                .findFirst()
                .orElse(null);
    }

    private Set<Node> getAnnotationsOfType(Set<Node> forest, Class annotation) {
        Set<Node> result = new HashSet<>();
        ForestUtils.traverse(forest, node -> {
            if(node.is(JCAnnotation.class) && node.getObject().type.tsym.getQualifiedName().contentEquals(annotation.getName())) {
                result.add(node);
            }
        });
        return result;
    }

    private Map<String, Node<JCClassDecl>> getAnnotationsDeclMap(Set<Node> forest) {
        Map<String, Node<JCClassDecl>> result = new HashMap<>();
        ForestUtils.traverse(forest, node -> {
            if(isAnnotationType(node)) {
                result.put(((JCClassDecl) node.getObject()).sym.type.tsym.getQualifiedName().toString(), node);
            }
        });
        return result;
    }

    private Map<KendalHandler, Set<Node>> getHandlerAnnotationsMap(Set<KendalHandler> handlers, Set<Node> forest) {
        long startTime = System.currentTimeMillis();
        Map<KendalHandler, Set<Node>> handlersWithNodes = handlers.stream()
                .collect(Collectors.toMap(Function.identity(), h -> new HashSet<>()));

        Map<String, KendalHandler> handlerAnnotationMap = handlers.stream()
                .filter(kendalHandler -> kendalHandler.getHandledAnnotationType() != null)
                .collect(Collectors.toMap(h -> h.getHandledAnnotationType().getName(), Function.identity()));

        while(handlerAnnotationMap.size() > 0) {
            Map<String, KendalHandler> newHandlerAnnotationMap = new HashMap<>();
            Map<String, KendalHandler> finalHandlerAnnotationMap = handlerAnnotationMap;
            ForestUtils.traverse(forest, node -> {
                if (node.is(JCAnnotation.class)) {
                    finalHandlerAnnotationMap.forEach((annotationName, handler) ->  {
                        if (annotationNameMatches(node, annotationName)) {
                            if (isPutOnAnnotation(node)) {
                                newHandlerAnnotationMap.put(((JCClassDecl) node.getParent().getObject()).sym.type.tsym.getQualifiedName().toString(), handler);
                            }
                            handlersWithNodes.get(handler).add(node);
                            messager.printMessage(Diagnostic.Kind.NOTE, String.format("annotation %s handled by %s",
                                    node.getObject().toString(), handler.getClass().getName()));
                        }
                    });
                }
            });
            handlerAnnotationMap = newHandlerAnnotationMap;
        }

        messager.printElapsedTime("Annotations' scanner", startTime);
        return handlersWithNodes;
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