package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class BlockNode extends Node<JCTree.JCBlock> {

    public BlockNode(JCTree.JCBlock object) {
        super(object);
    }

    public BlockNode(JCTree.JCBlock object, List<Node> children) {
        super(object, children);
    }

    public BlockNode(JCTree.JCBlock object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public BlockNode(JCTree.JCBlock object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
