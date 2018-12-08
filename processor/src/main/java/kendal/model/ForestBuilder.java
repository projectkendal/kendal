package kendal.model;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.Element;

import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;

import kendal.utils.KendalMessager;

public class ForestBuilder {

    private final Trees trees;
    private final KendalMessager messager;

    public ForestBuilder(Trees trees, KendalMessager messager) {
        this.trees = trees;
        this.messager = messager;
    }

    public Set<Node> buildForest(Set<? extends Element> elements) {
        long startTime = System.currentTimeMillis();
        final Set<JCCompilationUnit> compilationUnits = toCompilationUnits(elements);
        Set<Node> forest = new HashSet<>();
        compilationUnits.forEach(compilationUnit -> forest.add(TreeBuilder.buildTree(compilationUnit)));
        TreeBuilder.finishInitialPhase();
        messager.printElapsedTime("ForestBuilder", startTime);
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
