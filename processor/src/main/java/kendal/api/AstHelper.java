package kendal.api;

import com.sun.tools.javac.tree.JCTree;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

/*
*  Interface for AST modification helper class
*  Instance of AstHelper is passed to implementations of {@link kendal.api.KendalHandler}
*
* */
public interface AstHelper {
    // MODIFICATION METHODS
    void addVariableDeclarationToClass(Node<JCTree.JCClassDecl> clazz, Node<JCTree.JCVariableDecl> variableDeclaration) throws ImproperNodeTypeException;

    /**
     * Adds expression statement on the end of the method.
     */
    void addExpressionStatementToMethod(Node<JCTree.JCMethodDecl> method, Node<JCTree.JCExpressionStatement> expressionStatement) throws ImproperNodeTypeException;

    // SPECIFIC HELPERS
    AstNodeBuilder getAstNodeBuilder();
    AstValidator getAstValidator();
    AstUtils getAstUtils();
}
