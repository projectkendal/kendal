package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class DoWhileLoopNode extends Node<JCTree.JCDoWhileLoop> {

    public DoWhileLoopNode(JCTree.JCDoWhileLoop object) {
        super(object);
    }

    public DoWhileLoopNode(JCTree.JCDoWhileLoop object, List<Node> children) {
        super(object, children);
    }

    public DoWhileLoopNode(JCTree.JCDoWhileLoop object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public DoWhileLoopNode(JCTree.JCDoWhileLoop object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
