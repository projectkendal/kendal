package kendal.processor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;

import kendal.api.AstHelper;
import kendal.api.KendalHandler;
import kendal.api.exceptions.InvalidAnnotationException;
import kendal.api.impl.AstHelperImpl;
import kendal.model.ForestBuilder;
import kendal.model.Node;
import kendal.utils.ForestUtils;

import static kendal.utils.Utils.with;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class KendalProcessor extends AbstractProcessor {

    private Context context;
    private Trees trees;
    private ForestBuilder forestBuilder;
    private Messager messager;
    private AstHelper astHelper;


    @Override
    public void init(ProcessingEnvironment processingEnv) {
        JavacProcessingEnvironment javacProcEnv = (JavacProcessingEnvironment) processingEnv;
        context = javacProcEnv.getContext();
        trees = Trees.instance(processingEnv);
        messager = processingEnv.getMessager();
        forestBuilder = new ForestBuilder(trees);
        astHelper = new AstHelperImpl(context);
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.getRootElements().isEmpty()) return false;
        messager.printMessage(Diagnostic.Kind.NOTE, "Processor run!");
        Set<Node> forest = forestBuilder.buildForest(roundEnv.getRootElements());
        Set<KendalHandler> handlers = getHandlersFromSPI();
        registerHandlers(handlers);
        executeHandlers(getHandlerAnnotationsMap(handlers, forest));

        return false;
    }

    private Map<KendalHandler, Set<Node>> getHandlerAnnotationsMap(Set<KendalHandler> handlers, Set<Node> forest) {
        Map<KendalHandler, Set<Node>> result = handlers.stream().collect(Collectors.toMap(Function.identity(), h -> new HashSet<>()));
        ForestUtils.traverse(forest, node -> {
            if(node.getObject() instanceof JCTree.JCAnnotation) {
                handlers.forEach(handler -> {
                    with((JCTree.JCAnnotation)node.getObject(), jcAnnotation -> {
                        // TODO make proper resolution, this is just for now
                        if(handler.getHandledAnnotationType().getSimpleName().equals(jcAnnotation.annotationType.toString())) {
                            result.get(handler).add(node);
                            messager.printMessage(Diagnostic.Kind.NOTE, String.format("annotation %s handled by %s", jcAnnotation.toString(), handler.getClass().getName()));
                        }
                    });
                });
            }
        });

        return result;
    }

    private Set<KendalHandler> getHandlersFromSPI() {
        return StreamSupport
                .stream(ServiceLoader.load(KendalHandler.class, KendalProcessor.class.getClassLoader()).spliterator(), false)
                .collect(Collectors.toSet());
    }

    private void registerHandlers(Set<KendalHandler> handlers) {
        messager.printMessage(Diagnostic.Kind.NOTE, "### Kendal handles registration ###");
        handlers.forEach(handler ->
            messager.printMessage(Diagnostic.Kind.NOTE,
                    String.format("%s registered as provider for %s", handler.getClass().getName(), handler.getHandledAnnotationType()))
        );
    }

    private void executeHandlers(Map<KendalHandler, Set<Node>> handlersMap) {
        messager.printMessage(Diagnostic.Kind.NOTE, "### Kendal handles execution ###");
        handlersMap.entrySet().forEach(entry -> {
            try {
                entry.getKey().handle(entry.getValue(), astHelper);
            } catch (InvalidAnnotationException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        });
    }



}