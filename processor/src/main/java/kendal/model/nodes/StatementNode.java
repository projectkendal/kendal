package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class StatementNode <T extends JCTree.JCStatement> extends Node<T> {

    public StatementNode(T object) {
        super(object);
    }

    public StatementNode(T object, List<Node> children) {
        super(object, children);
    }

    public StatementNode(T object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public StatementNode(T object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
