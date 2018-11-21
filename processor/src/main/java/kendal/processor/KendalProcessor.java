package kendal.processor;

import static kendal.utils.Utils.with;

import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
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
import javax.tools.JavaFileObject;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.DocPretty;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.Pretty;
import com.sun.tools.javac.util.Context;

import kendal.api.AstHelper;
import kendal.api.KendalHandler;
import kendal.api.exceptions.KendalException;
import kendal.api.impl.AstHelperImpl;
import kendal.model.Node;
import kendal.model.builders.ForestBuilder;
import kendal.utils.ForestUtils;

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
        messager.printMessage(Diagnostic.Kind.NOTE, "Processor run!");
        if (roundEnv.getRootElements().isEmpty()) return false;
        if (roundEnv.processingOver()) return false;
        Set<Node> forest = forestBuilder.buildForest(roundEnv.getRootElements());
        Set<KendalHandler> handlers = getHandlersFromSPI();
        registerHandlers(handlers);
        executeHandlers(getHandlerAnnotationsMap(handlers, forest));

        return false;
    }

    private Map<KendalHandler, Set<Node>> getHandlerAnnotationsMap(Set<KendalHandler> handlers, Set<Node> forest) {
        Map<KendalHandler, Set<Node>> result = handlers.stream().collect(Collectors.toMap(Function.identity(), h -> new HashSet<>()));
        ForestUtils.traverse(forest, node -> {
            if(node.getObject() instanceof JCAnnotation) {
                handlers.forEach(handler -> {
                    with((JCAnnotation)node.getObject(), jcAnnotation -> {
                        // TODO This will PROBABLY work, but we have to make sure
                        if(jcAnnotation.type.tsym.getQualifiedName().contentEquals(handler.getHandledAnnotationType().getName())) {
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
        handlersMap.forEach((key, value) -> {
            try {
                key.handle(value, astHelper);
            } catch (KendalException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        });
    }



}