package kendal.model;

import java.util.List;

import com.sun.tools.javac.tree.JCTree;

public class Node {
    JCTree object;
    Node parent;
    List<Node> children;

    public Node(JCTree object) {
        this.object = object;
    }

    public Node(JCTree object, List<Node> children) {
        this(object);
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

    public void addChild(Node newChild) {
        children.add(newChild);
    }

    public void addChild(int index, Node newChild) {
        children.add(index, newChild);
    }
}
