package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class CompilationUnitNode extends Node<JCTree.JCCompilationUnit> {

    public CompilationUnitNode(JCTree.JCCompilationUnit object) {
        super(object);
    }

    public CompilationUnitNode(JCTree.JCCompilationUnit object, List<Node> children) {
        super(object, children);
    }

    public CompilationUnitNode(JCTree.JCCompilationUnit object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public CompilationUnitNode(JCTree.JCCompilationUnit object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
