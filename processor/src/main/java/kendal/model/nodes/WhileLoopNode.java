package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class WhileLoopNode extends Node<JCTree.JCWhileLoop> {

    public WhileLoopNode(JCTree.JCWhileLoop object) {
        super(object);
    }

    public WhileLoopNode(JCTree.JCWhileLoop object, List<Node> children) {
        super(object, children);
    }

    public WhileLoopNode(JCTree.JCWhileLoop object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public WhileLoopNode(JCTree.JCWhileLoop object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
