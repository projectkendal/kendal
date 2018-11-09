package kendal.model;

import java.util.Set;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;

import kendal.processor.KendalProcessor;

public class Node {
    JCTree object;
    Node parent;
    Set<Node> children;

    public Node(JCTree object, Set<Node> children) {
        this.object = object;
        this.children = children;
        children.forEach(child -> child.parent = this);
        // TODO: remove below when annotations are collected properly
        children.forEach(child -> {
            if (child.object instanceof JCAnnotation) {
                KendalProcessor.annotatedElement = this;
            }
        });
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
