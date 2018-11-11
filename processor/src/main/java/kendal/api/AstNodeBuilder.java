package kendal.api;

import com.sun.tools.javac.util.Name;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;
import kendal.model.nodes.ExpressionStatementNode;
import kendal.model.nodes.FieldAccessNode;
import kendal.model.nodes.IdentifierNode;
import kendal.model.nodes.VariableDefNode;

public interface AstNodeBuilder {
    VariableDefNode buildVariableDecl(Modifier modifier, Object type, Name Name);
    IdentifierNode buildObjectReference(Name fieldName);
    FieldAccessNode buildFieldAccess(IdentifierNode objectRef, Name fieldName) throws ImproperNodeTypeException;

    // ExpressionStatements:
    ExpressionStatementNode buildAssignmentStatement(Node lhs, Node rhs) throws ImproperNodeTypeException;
}
