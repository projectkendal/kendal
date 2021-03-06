package kendal.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.sun.tools.javac.tree.JCTree;

import kendal.api.AstHelper;
import kendal.utils.ForestUtils;

public class Node <T extends JCTree> {
    private static boolean isInitialPhase = true;

    private T object;
    private Node parent;
    private List<Node> children;
    private final boolean addedByHandler;

    Node(T object) {
        this(object, new ArrayList<>());
    }

    Node(T object, List<Node> children) {
        this.object = object;
        this.children = children;
        children.forEach(child -> child.parent = this);
        this.addedByHandler = !isInitialPhase;
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

    public boolean isAddedByHandler() {
        return addedByHandler;
    }

    public void addChild(Node newChild, AstHelper.Mode mode, int offset) {
        if (AstHelper.Mode.APPEND.equals(mode)) {
            offset = this.children.size() - offset;
        }
        children.add(offset, newChild);
        newChild.parent = this;
    }

    public void removeChild(Node child) {
        children.remove(child);
    }

    public boolean is(Class clazz) {
        return clazz.isInstance(object);
    }

    static void finishInitialPhase() {
        isInitialPhase = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node<?> node = (Node<?>) o;
        return Objects.equals(object, node.object);
    }

    @Override
    public int hashCode() {
        return Objects.hash(object);
    }
}
