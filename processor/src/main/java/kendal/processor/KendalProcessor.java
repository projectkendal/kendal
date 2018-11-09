package kendal.processor;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
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
import com.sun.tools.javac.util.Context;

import kendal.api.KendalHandler;
import kendal.api.impl.AstHelperImpl;
import kendal.model.ForestBuilder;
import kendal.model.Node;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class KendalProcessor extends AbstractProcessor {

    private Context context;
    private Trees trees;
    private ForestBuilder forestBuilder;
    private Messager messager;

    // TODO: remove variable below when annotations are collected properly
    public static Set<Node> annotatedNodes = new HashSet<>();

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        JavacProcessingEnvironment javacProcEnv = (JavacProcessingEnvironment) processingEnv;
        context = javacProcEnv.getContext();
        trees = Trees.instance(processingEnv);
        messager = processingEnv.getMessager();
        forestBuilder = new ForestBuilder(trees);
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.getRootElements().isEmpty()) return false;
        messager.printMessage(Diagnostic.Kind.NOTE, "Processor run!");
        Set<Node> forest = forestBuilder.buildForest(roundEnv.getRootElements());
        Set<KendalHandler> handlers = getHandlersFromSPI();
        registerHandlers(handlers);
        executeHandlers(handlers);

        return false;
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

    private void executeHandlers(Set<KendalHandler> handlers) {
        messager.printMessage(Diagnostic.Kind.NOTE, "### Kendal handles execution ###");
        handlers.forEach(handler -> {
            // TODO: when handlers are properly handled (they are only called for their annotation) remove if below
            if (handler.getHandledAnnotationType().getName().equals("kendal.annotations.Protected")) {
                handler.handle(annotatedNodes, new AstHelperImpl(context));
            }
        });
    }



}