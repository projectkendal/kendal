package kendal.api;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;
import kendal.model.nodes.ClassNode;
import kendal.model.nodes.ExpressionStatementNode;
import kendal.model.nodes.MethodNode;
import kendal.model.nodes.VariableDefNode;

/*
*  Interface for AST modification helper class
*  Instance of AstHelper is passed to implementations of {@link kendal.api.KendalHandler}
*
* */
public interface AstHelper {
    // MODIFICATION METHODS
    void addVariableDeclarationToClass(ClassNode clazz, VariableDefNode variableDeclaration) throws ImproperNodeTypeException;

    /**
     * Adds expression statement on the end of the method.
     */
    void addExpressionStatementToMethod(MethodNode method, ExpressionStatementNode expressionStatement) throws ImproperNodeTypeException;

    // SPECIFIC HELPERS
    AstNodeBuilder getAstNodeBuilder();
    AstValidator getAstValidator();
    AstUtils getAstUtils();
}
