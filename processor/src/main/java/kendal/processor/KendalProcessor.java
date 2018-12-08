package kendal.processor;

import static kendal.utils.Utils.with;

import java.util.HashSet;
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
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
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
import kendal.utils.interpolation.StringInterpolator;

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
        registerHandlers(handlers);
        executeHandlers(getHandlerAnnotationsMap(handlers, forest));

        new StringInterpolator(context).interpolate(forest);

        messager.printElapsedTime("Processor", startTime);
        return false;
    }

    private Map<KendalHandler, Set<Node>> getHandlerAnnotationsMap(Set<KendalHandler> handlers, Set<Node> forest) {
        long startTime = System.currentTimeMillis();
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

        messager.printElapsedTime("Annotations' scanner", startTime);
        return result;
    }

    private Set<KendalHandler> getHandlersFromSPI() {
        return StreamSupport
                .stream(ServiceLoader.load(KendalHandler.class, KendalProcessor.class.getClassLoader()).spliterator(), false)
                .collect(Collectors.toSet());
    }

    private void registerHandlers(Set<KendalHandler> handlers) {
        messager.printMessage(Diagnostic.Kind.NOTE, "### Handlers' registration ###");
        handlers.forEach(handler ->
            messager.printMessage(Diagnostic.Kind.NOTE,
                    String.format("%s registered as provider for %s", handler.getClass().getName(), handler.getHandledAnnotationType()))
        );
    }

    private void executeHandlers(Map<KendalHandler, Set<Node>> handlersMap) {
        messager.printMessage(Diagnostic.Kind.NOTE, "### Handlers' execution ###");
        handlersMap.forEach((handler, nodes) -> {
            try {
                long startTime = System.currentTimeMillis();
                handler.handle(nodes, astHelper);
                messager.printElapsedTime("Handler" + handler.getClass().getName(), startTime);
            } catch (KendalException | KendalRuntimeException e) {
                messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            }
        });
    }

}