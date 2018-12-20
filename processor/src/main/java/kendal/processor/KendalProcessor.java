package kendal.processor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.util.Context;

import kendal.api.AstHelper;
import kendal.api.KendalHandler;
import kendal.api.exceptions.KendalException;
import kendal.api.exceptions.KendalRuntimeException;
import kendal.api.impl.AstHelperImpl;
import kendal.model.ForestBuilder;
import kendal.model.Node;
import kendal.utils.ForestUtils;
import kendal.utils.KendalMessager;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class KendalProcessor extends AbstractProcessor {

    private Context context;
    private Trees trees;
    private ForestBuilder forestBuilder;
    private KendalMessager messager;
    private AstHelper astHelper;


    @Override
    public void init(ProcessingEnvironment processingEnv) {
        JavacProcessingEnvironment javacProcEnv = (JavacProcessingEnvironment) processingEnv;
        context = javacProcEnv.getContext();
        trees = Trees.instance(processingEnv);
        messager = new KendalMessager(processingEnv.getMessager());
        forestBuilder = new ForestBuilder(trees, messager);
        astHelper = new AstHelperImpl(context);
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        long startTime = System.currentTimeMillis();
        messager.printMessage(Diagnostic.Kind.NOTE, "Processor run!");
        if (roundEnv.getRootElements().isEmpty()) return false;
        if (roundEnv.processingOver()) return false;
        Set<Node> forest = forestBuilder.buildForest(roundEnv.getRootElements());
        Set<KendalHandler> handlers = getHandlersFromSPI();
        displayRegisteredHandlers(handlers);
        executeHandlers(getHandlerAnnotationsMap(handlers, forest), forest);

        messager.printElapsedTime("Processor", startTime);
        return false;
    }

    private Map<KendalHandler, Set<Node>> getHandlerAnnotationsMap(Set<KendalHandler> handlers, Set<Node> forest) {
        long startTime = System.currentTimeMillis();
        Map<KendalHandler, Set<Node>> handlersWithNodes = handlers.stream()
                .collect(Collectors.toMap(Function.identity(), h -> new HashSet<>()));
        // map of annotations that were annotated by a handled annotation as a key with this annotation added as a value
        List<WrapperAnnotation> wrapperAnnotations = new LinkedList<>();
        ForestUtils.traverse(forest, node -> {
            if (node.is(JCAnnotation.class)) {
                handlers.forEach(handler -> {
                    if (handler.getHandledAnnotationType() == null) return;
                    if (isAnnotationHandledByHandler(node, handler)) {
                        if (isPutOnAnnotation(node)) {
                            wrapperAnnotations.add(new WrapperAnnotation(node.getParent(), handler));
                        }
                        else {
                            handlersWithNodes.get(handler).add(node);
                            messager.printMessage(Diagnostic.Kind.NOTE, String.format("annotation %s handled by %s",
                                    node.getObject().toString(), handler.getClass().getName()));
                        }
                    }
                });
            }
        });

        ForestUtils.traverse(forest, node -> {
            if (node.is(JCAnnotation.class)) {
                wrapperAnnotations.forEach(wrapperAnnotation -> {
                    if (node.getObject().type.tsym == wrapperAnnotation.annotation.getObject().sym) {
                        handlersWithNodes.get(wrapperAnnotation.handler).add(node);
                        messager.printMessage(Diagnostic.Kind.NOTE, String.format("annotation %s handled by %s",
                                node.getObject().toString(), wrapperAnnotation.handler.getClass().getName()));
                    }
                });
            }
        });

        messager.printElapsedTime("Annotations' scanner", startTime);
        return handlersWithNodes;
    }

    private boolean isAnnotationHandledByHandler(Node<JCAnnotation> annotationNode, KendalHandler handler) {
        return annotationNode.getObject().type.tsym.getQualifiedName().contentEquals(handler.getHandledAnnotationType().getName());
    }

    private boolean isPutOnAnnotation(Node<JCAnnotation> annotationNode) {
        JCTree parent = annotationNode.getParent().getObject();
        return parent instanceof JCClassDecl && (((JCClassDecl) parent).mods.flags & Flags.ANNOTATION) != 0;
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

    /**
     * Represents annotation which is used as a wrapper for original annotation that is handled by some handler.
     */
    private class WrapperAnnotation {
        final Node<JCClassDecl> annotation;
        final KendalHandler handler;

        WrapperAnnotation(Node<JCClassDecl> annotation, KendalHandler handler) {
            this.annotation = annotation;
            this.handler = handler;
        }
    }
}