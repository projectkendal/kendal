package kendal.model;

import com.sun.tools.javac.tree.JCTree;

import java.util.Set;

public class Node {
    JCTree object;
    Node parent;
    Set<Node> children;

    public Node(JCTree object, Set<Node> children) {
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

    public Set<Node> getChildren() {
        return children;
    }
}
