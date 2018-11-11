package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class AnnotationNode extends Node<JCTree.JCAnnotation> {

    public AnnotationNode(JCTree.JCAnnotation object) {
        super(object);
    }

    public AnnotationNode(JCTree.JCAnnotation object, List<Node> children) {
        super(object, children);
    }

    public AnnotationNode(JCTree.JCAnnotation object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public AnnotationNode(JCTree.JCAnnotation object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
