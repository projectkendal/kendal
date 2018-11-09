package kendal.api;

import kendal.model.Node;

public interface AstNodeBuilder {
    Node buildVariableDecl(Modifier modifier, Object type, String rawName);
}
