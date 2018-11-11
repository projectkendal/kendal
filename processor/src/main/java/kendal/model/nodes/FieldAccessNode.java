package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class FieldAccessNode extends Node<JCTree.JCFieldAccess> {

    public FieldAccessNode(JCTree.JCFieldAccess object) {
        super(object);
    }

    public FieldAccessNode(JCTree.JCFieldAccess object, List<Node> children) {
        super(object, children);
    }

    public FieldAccessNode(JCTree.JCFieldAccess object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public FieldAccessNode(JCTree.JCFieldAccess object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
