package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class VariableDefNode extends Node<JCTree.JCVariableDecl> {

    public VariableDefNode(JCTree.JCVariableDecl object) {
        super(object);
    }

    public VariableDefNode(JCTree.JCVariableDecl object, List<Node> children) {
        super(object, children);
    }

    public VariableDefNode(JCTree.JCVariableDecl object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public VariableDefNode(JCTree.JCVariableDecl object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
