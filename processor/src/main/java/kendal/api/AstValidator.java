package kendal.api;

import kendal.model.Node;

public interface AstValidator {
    boolean isClassDecl(Node node);
    boolean isVariableDecl(Node node);
    boolean isExpressionStatement(Node node);
    boolean isMethodDecl(Node node);
    boolean isConstructorDecl(Node node);
}
