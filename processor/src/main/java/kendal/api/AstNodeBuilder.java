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
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import kendal.api.builders.BlockBuilder;
import kendal.api.builders.FieldAccessBuilder;
import kendal.api.builders.IdentifierBuilder;
import kendal.api.builders.MethodDeclBuilder;
import kendal.api.builders.MethodInvocationBuilder;
import kendal.api.builders.TryBuilder;
import kendal.api.builders.VariableDeclBuilder;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

public interface AstNodeBuilder {

    VariableDeclBuilder variableDecl();
    MethodDeclBuilder methodDecl();
    MethodInvocationBuilder methodInvocation();
    FieldAccessBuilder fieldAccess();
    BlockBuilder block();
    IdentifierBuilder identifier();
    TryBuilder tryBlock();

    /**
     * Constructs field accessor for more complex expressions (the ones using dots).
     * Returns either {@link Node<JCIdent>} or {@link Node<JCFieldAccess>}.
     */
    Node<JCExpression> getAccessor(String fullName);

    <T extends JCExpression> Node<JCReturn> buildReturnStatement(Node<T> expression);

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
