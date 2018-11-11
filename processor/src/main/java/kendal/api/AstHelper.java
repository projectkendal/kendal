package kendal.api;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

/*
*  Interface for AST modification helper class
*  Instance of AstHelper is passed to implementations of {@link kendal.api.KendalHandler}
*
* */
public interface AstHelper {
    // MODIFICATION METHODS
    void addVariableDeclarationToClass(Node clazz, Node variableDeclaration) throws ImproperNodeTypeException;

    /**
     * Adds expression statement on the end of the method.
     */
    void addExpressionStatementToMethod(Node method, Node expressionStatement) throws ImproperNodeTypeException;

    // SPECIFIC HELPERS
    AstNodeBuilder getAstNodeBuilder();
    AstValidator getAstValidator();
    AstUtils getAstUtils();
}
