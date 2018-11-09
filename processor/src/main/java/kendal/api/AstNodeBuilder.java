package kendal.api;

import kendal.model.Node;

public interface AstNodeBuilder {
    Node buildVariableDecl(String name, Object type);
}
