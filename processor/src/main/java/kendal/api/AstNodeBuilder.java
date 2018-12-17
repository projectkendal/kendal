package kendal.api;

import java.util.List;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTry;
import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.api.builders.VariableDecl;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

public interface AstNodeBuilder {

    VariableDecl variableDecl();

    Node<JCMethodDecl> buildMethodDecl(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typarams,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            Node<JCBlock> body);
    Node<JCMethodDecl> buildMethodDecl(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCTree.JCTypeParameter> typarams,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            JCBlock body);

    <T extends JCExpression>Node<JCMethodInvocation> buildMethodInvocation(Node<T> method);
    <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> buildMethodInvocation(Node<T> method,
            List<Node<P>> parameters);
    <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> buildMethodInvocation(Node<T> method,
            Node<P> parameter);
    <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> buildMethodInvocation(Node<T> method,
            com.sun.tools.javac.util.List<P> parameters);

    <T extends JCExpression> Node<JCFieldAccess> buildFieldAccess(Node<T> objectRef, String fieldName);
    <T extends JCExpression> Node<JCFieldAccess> buildFieldAccess(Node<T> objectRef, Name fieldName);

    /**
     * Constructs field accessor for more complex expressions (the ones using dots).
     * Returns either {@link Node<JCIdent>} or {@link Node<JCFieldAccess>}.
     */
    Node<JCExpression> getAccessor(String fullName);

    <T extends JCExpression> Node<JCReturn> buildReturnStatement(Node<T> expression);

    // todo: think if we want to move such similar methods to separate like for example "blockBuilder", etc... https://trello.com/c/OGWsNTO0/47-astnodebuilder-consider-split-into-smaller-builders
    <T extends JCStatement> Node<JCBlock> buildBlock(List<Node<T>> statements);
    <T extends JCStatement> Node<JCBlock> buildBlock(Node<T> statement);
    <T extends JCStatement> Node<JCBlock> buildBlock(com.sun.tools.javac.util.List<T> statements);

    Node<JCIdent> buildIdentifier(String name);
    Node<JCIdent> buildIdentifier(Name name);

    Node<JCTry> buildTry(Node<JCBlock> body, Node<JCCatch> catchers);
    Node<JCTry> buildTry(Node<JCBlock> body, List<Node<JCCatch>> catchers);
    Node<JCTry> buildTry(Node<JCBlock> body, com.sun.tools.javac.util.List<JCCatch> catchers);

    Node<JCCatch> buildCatch(Node<JCVariableDecl> param, Node<JCBlock> body);

    <T extends JCExpression> Node<JCThrow> buildThrow(Node<T> expression);

    <T extends JCExpression> Node<JCNewClass> buildNewClass(Node<JCIdent> clazz, Node<T> arg);
    <T extends JCExpression> Node<JCNewClass> buildNewClass(Node<JCIdent> clazz, List<Node<T>> args);

    <T extends JCExpression> Node<JCTypeUnion> buildTypeUnion(List<Node<T>> components);

    JCExpression buildType(Type type);

    JCBinary buildBinary(JCTree.Tag opcode, JCExpression lhs, JCExpression rhs);

    JCLiteral buildLiteral(String value);

    // ExpressionStatements:
    <L extends JCExpression, R extends JCExpression> Node<JCExpressionStatement> buildAssignmentStatement(Node<L> lhs,
            Node<R> rhs) throws ImproperNodeTypeException;
}
