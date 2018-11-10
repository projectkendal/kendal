package kendal.api;

import kendal.model.Node;

public interface AstValidator {
    boolean isMethodDecl(Node node);
    boolean isConstructorDecl(Node node);
}
