package kendal.api;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

/*
*  Interface for AST modification helper class
*  Instance of AstHelper is passed to implementations of {@link kendal.api.KendalHandler}
*
* */
public interface AstHelper {
    // MODIFICATION METHODS
    void addVariableDeclarationToClass(Node<JCClassDecl> clazz, Node<JCVariableDecl> variableDeclaration) throws ImproperNodeTypeException;

    /**
     * Adds expression statement on the end of the method.
     */
    <T extends JCExpressionStatement> void addExpressionStatementToMethod(Node<JCMethodDecl> method,
            Node<T> expressionStatement) throws ImproperNodeTypeException;

    // SPECIFIC HELPERS
    AstNodeBuilder getAstNodeBuilder();
    AstValidator getAstValidator();
    AstUtils getAstUtils();
}
