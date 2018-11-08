package kendal.processor;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;
import kendal.api.KendalHandler;
import kendal.model.Node;
import kendal.model.TreeBuilder;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.stream.StreamSupport;

@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class KendalProcessor extends AbstractProcessor {

    private Context context;
    private Trees trees;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        JavacProcessingEnvironment javacProcEnv = (JavacProcessingEnvironment) processingEnv;
        context = javacProcEnv.getContext();
        trees = Trees.instance(processingEnv);
        super.init(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Processor run!");
        registerHandlers();
        Set<Node> forest = buildForest(roundEnv.getRootElements());

        return false;
    }

    private void registerHandlers() {
        StreamSupport.stream(ServiceLoader.load(KendalHandler.class, KendalProcessor.class.getClassLoader()).spliterator(), false).forEach(kendalHandler -> {
            this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
                    String.format("%s registered as provider for %s", kendalHandler.getClass().getName(), kendalHandler.getHandledAnnotationType()));

        });
    }

    private Set<Node> buildForest(Set<? extends Element> elements) {
        final Set<JCCompilationUnit> compilationUnits = toCompilationUnits(elements);
        Set<Node> forest = new HashSet<>();
        compilationUnits.forEach(compilationUnit -> forest.add(TreeBuilder.buildTree(compilationUnit)));
        return forest;
    }

    private Set<JCCompilationUnit> toCompilationUnits(Set<? extends Element> elements) {
        final Set<JCCompilationUnit> compilationUnits = new HashSet<>();
        for (Element element : elements) {
            JCCompilationUnit unit = toUnit(element);
            if (unit == null) continue;
            compilationUnits.add(unit);
        }
        return compilationUnits;
    }

    private JCCompilationUnit toUnit(Element element) {
        TreePath path = trees == null ? null : trees.getPath(element);
        return path != null ? (JCCompilationUnit) path.getCompilationUnit() : null;
    }

}