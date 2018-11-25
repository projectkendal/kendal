package kendal.model;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.tree.JCTree;

public class Node <T extends JCTree> {
    private T object;
    private Node parent;
    private List<Node> children;
    private final boolean addedByKendal;

    public Node(T object) {
        this(object, new ArrayList<>());
    }

    public Node(T object, List<Node> children) {
        this(object, children, false);
    }

    public Node(T object, boolean addedByKendal) {
        this(object, new ArrayList<>(), addedByKendal);
    }

    public Node(T object, List<Node> children, boolean addedByKendal) {
        this.object = object;
        this.children = children;
        children.forEach(child -> child.parent = this);
        this.addedByKendal = addedByKendal;
    }

    public T getObject() {
        return object;
    }

    public Node getParent() {
        return parent;
    }

    public List<Node> getChildren() {
        return children;
    }

    public boolean isAddedByKendal() {
        return addedByKendal;
    }

    public void addChild(Node newChild) {
        children.add(newChild);
        newChild.parent = this;
    }

    public void addChild(int index, Node newChild) {
        children.add(index, newChild);
        newChild.parent = this;
    }
}
