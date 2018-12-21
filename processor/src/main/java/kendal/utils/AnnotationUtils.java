package kendal.utils;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

public class AnnotationUtils {

    private AnnotationUtils() {

    }

    public static boolean isPutOnAnnotation(Node<JCTree.JCAnnotation> annotationNode) {
        JCTree parent = annotationNode.getParent().getObject();
        return parent instanceof JCTree.JCClassDecl && (((JCTree.JCClassDecl) parent).mods.flags & Flags.ANNOTATION) != 0;
    }

    public static boolean annotationNameMatches(Node<JCTree.JCAnnotation> annotationNode, String annotationName) {
        return annotationNode.getObject().type.tsym.getQualifiedName().contentEquals(annotationName);
    }
}
