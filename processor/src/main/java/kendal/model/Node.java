package kendal.model;

import com.sun.tools.javac.tree.JCTree;

import java.util.Set;

public class Node {
    JCTree object;
    Node parent;
    Set<Node> children;

    Node(JCTree object, Set<Node> children) {
        this.object = object;
        this.children = children;
        children.forEach(child -> child.parent = this);
    }
}
