package kendal.api;

import com.sun.tools.javac.util.Name;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

public interface AstNodeBuilder {
    Node buildVariableDecl(Modifier modifier, Object type, Name Name);
    Node buildObjectReference(Name fieldName);
    Node buildFieldAccess(Node objectRef, Name fieldName) throws ImproperNodeTypeException;

    // ExpressionStatements:
    Node buildAssignmentStatement(Node lhs, Node rhs) throws ImproperNodeTypeException;
}
