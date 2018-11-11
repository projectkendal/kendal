package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class MethodNode extends Node<JCTree.JCMethodDecl> {

    public MethodNode(JCTree.JCMethodDecl object) {
        super(object);
    }

    public MethodNode(JCTree.JCMethodDecl object, List<Node> children) {
        super(object, children);
    }

    public MethodNode(JCTree.JCMethodDecl object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public MethodNode(JCTree.JCMethodDecl object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
