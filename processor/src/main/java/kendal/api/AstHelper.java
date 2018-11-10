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
    void addExpressionStatementToMethod(Node method, Node expressionStatement, int lineIndex)
            throws ImproperNodeTypeException;

    // SPECIFIC HELPERS
    AstNodeBuilder getAstNodeBuilder();
    AstValidator getAstValidator();
}
