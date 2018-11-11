package kendal.api;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Name;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

public interface AstNodeBuilder {
    Node<JCTree.JCVariableDecl> buildVariableDecl(Modifier modifier, Object type, Name Name);
    Node<JCTree.JCIdent> buildObjectReference(Name fieldName);
    Node<JCTree.JCFieldAccess> buildFieldAccess(Node<JCTree.JCIdent> objectRef, Name fieldName) throws ImproperNodeTypeException;

    // ExpressionStatements:
    <L extends JCTree.JCExpression, R extends JCTree.JCExpression> Node<JCTree.JCExpressionStatement>
    buildAssignmentStatement(Node<L> lhs, Node<R> rhs) throws ImproperNodeTypeException;
}
