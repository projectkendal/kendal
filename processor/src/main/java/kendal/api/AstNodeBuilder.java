package kendal.api;

import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

public interface AstNodeBuilder {
    Node<JCVariableDecl> buildVariableDecl(List<Modifier> modifiers, JCExpression type, Name Name,
            Node<JCAnnotation> source);

    Node<JCMethodDecl> buildMethodDecl(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCVariableDecl> params, JCBlock body);

    Node<JCIdent> buildObjectReference(Name fieldName);

    Node<JCMethodInvocation> buildMethodInvocation(Node<JCExpression> method);

    Node<JCMethodInvocation> buildMethodInvocation(Node<JCExpression> method,
            com.sun.tools.javac.util.List<JCExpression> parameters);

    Node<JCFieldAccess> buildFieldAccess(Node<JCIdent> objectRef, Name fieldName);

    <T extends JCExpression> Node<JCReturn> buildReturnStatement(Node<T> expression);

    <T extends JCStatement> Node<JCBlock> buildBlock(List<Node<T>> statements);

    // ExpressionStatements:
    <L extends JCExpression, R extends JCExpression> Node<JCExpressionStatement> buildAssignmentStatement(Node<L> lhs,
            Node<R> rhs) throws ImproperNodeTypeException;
}
