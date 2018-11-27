package kendal.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.tools.javac.tree.JCTree;

import kendal.utils.ForestUtils;

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

    public <T extends JCTree> List<Node<T>> getChildrenOfType(Class<T> clazz) {
        return children.stream().filter(c -> clazz.isInstance(c.object)).map(c -> (Node<T>)c).collect(Collectors.toList());
    }

    /**
     * Gets children of given type, search all depths.
     */
    public <T extends JCTree> List<Node<T>> deepGetChildrenOfType(Class<T> clazz) {
        List<Node<T>> result = new LinkedList<>();
        ForestUtils.traverse(children, (child -> {
            if (clazz.isInstance(child.object)) {
                result.add(child);
            }
        }));
        return result;
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
