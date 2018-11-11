package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class ClassNode extends Node<JCTree.JCClassDecl> {


    public ClassNode(JCTree.JCClassDecl object) {
        super(object);
    }

    public ClassNode(JCTree.JCClassDecl object, List<Node> children) {
        super(object, children);
    }

    public ClassNode(JCTree.JCClassDecl object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public ClassNode(JCTree.JCClassDecl object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
