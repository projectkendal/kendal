package kendal.utils;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;

import kendal.model.Node;

public class AnnotationUtils {

    private AnnotationUtils() {

    }

    public static boolean isPutOnAnnotation(Node<JCAnnotation> annotationNode) {
        return isAnnotationType(annotationNode.getParent());
    }

    public static boolean isPutOnMethod(Node<JCAnnotation> annotationNode) {
        return annotationNode.getParent().getObject() instanceof JCTree.JCMethodDecl;
    }

    public static boolean isAnnotationType(Node node) {
        return node.getObject() instanceof  JCClassDecl && (((JCClassDecl) node.getObject()).mods.flags & Flags.ANNOTATION) != 0;
    }
}
