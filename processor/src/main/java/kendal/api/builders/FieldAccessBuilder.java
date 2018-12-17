package kendal.api.builders;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.util.Name;

import kendal.model.Node;

public interface FieldAccessBuilder {
    <T extends JCExpression> Node<JCFieldAccess> build(Node<T> objectRef, String fieldName);
    <T extends JCExpression> Node<JCFieldAccess> build(Node<T> objectRef, Name fieldName);

}
