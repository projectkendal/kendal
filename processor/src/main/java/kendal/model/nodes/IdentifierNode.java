package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class IdentifierNode extends Node<JCTree.JCIdent> {

    public IdentifierNode(JCTree.JCIdent object) {
        super(object);
    }

    public IdentifierNode(JCTree.JCIdent object, List<Node> children) {
        super(object, children);
    }

    public IdentifierNode(JCTree.JCIdent object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public IdentifierNode(JCTree.JCIdent object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
