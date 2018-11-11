package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class ForLoopNode extends Node<JCTree.JCForLoop> {

    public ForLoopNode(JCTree.JCForLoop object) {
        super(object);
    }

    public ForLoopNode(JCTree.JCForLoop object, List<Node> children) {
        super(object, children);
    }

    public ForLoopNode(JCTree.JCForLoop object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public ForLoopNode(JCTree.JCForLoop object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
