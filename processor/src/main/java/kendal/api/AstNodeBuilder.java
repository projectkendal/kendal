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
            com.sun.tools.javac.util.List<JCVariableDecl> params, Node<JCBlock> body);
    Node<JCMethodDecl> buildMethodDecl(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCVariableDecl> params, JCBlock body);

    Node<JCIdent> buildObjectReference(Name fieldName);

    <T extends JCExpression>Node<JCMethodInvocation> buildMethodInvocation(Node<T> method);
    <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> buildMethodInvocation(Node<T> method,
            List<Node<P>> parameters);
    <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> buildMethodInvocation(Node<T> method,
            com.sun.tools.javac.util.List<P> parameters);

    Node<JCFieldAccess> buildFieldAccess(Node<JCIdent> objectRef, Name fieldName);

    <T extends JCExpression> Node<JCReturn> buildReturnStatement(Node<T> expression);

    // todo: think if we want to move such similar methods to separate like for example "blockBuilder", etc...
    <T extends JCStatement> Node<JCBlock> buildBlock(List<Node<T>> statements);
    <T extends JCStatement> Node<JCBlock> buildBlock(Node<T> statement);
    <T extends JCStatement> Node<JCBlock> buildBlock(com.sun.tools.javac.util.List<T> statements);

    Node<JCIdent> buildIdentifier(String name);
    Node<JCIdent> buildIdentifier(Name name);

    // ExpressionStatements:
    <L extends JCExpression, R extends JCExpression> Node<JCExpressionStatement> buildAssignmentStatement(Node<L> lhs,
            Node<R> rhs) throws ImproperNodeTypeException;
}
