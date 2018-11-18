package kendal.api;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

import java.util.List;

public interface AstNodeBuilder {
    Node<JCVariableDecl> buildVariableDecl(List<Modifier> modifiers, Object type, Name Name);
    Node<JCIdent> buildObjectReference(Name fieldName);
    Node<JCFieldAccess> buildFieldAccess(Node<JCIdent> objectRef, Name fieldName) throws ImproperNodeTypeException;

    // ExpressionStatements:
    <L extends JCExpression, R extends JCExpression> Node<JCExpressionStatement>
    buildAssignmentStatement(Node<L> lhs, Node<R> rhs) throws ImproperNodeTypeException;
}
