package kendal.api;

import com.sun.tools.javac.util.Name;

import kendal.model.Node;

public interface AstNodeBuilder {
    Node buildVariableDecl(Modifier modifier, Object type, Name Name);
}
