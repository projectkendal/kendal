package kendal.api;

import kendal.model.Node;

public interface AstValidator {
    boolean isClass(Node node);
    boolean isVariable(Node node);
    boolean isExpression(Node node);
    boolean isIdent(Node node);
    boolean isExpressionStatement(Node node);
    boolean isMethod(Node node);
    boolean isConstructor(Node node);
}
