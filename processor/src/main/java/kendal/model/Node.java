package kendal.model;

import java.util.List;

import com.sun.tools.javac.tree.JCTree;

public class Node {
    JCTree object;
    Node parent;
    List<Node> children;

    public Node(JCTree object, List<Node> children) {
        this.object = object;
        this.children = children;
        children.forEach(child -> child.parent = this);
    }

    public JCTree getObject() {
        return object;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }
}
