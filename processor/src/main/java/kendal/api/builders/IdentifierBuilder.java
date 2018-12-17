package kendal.api.builders;

import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.util.Name;

import kendal.model.Node;

public interface IdentifierBuilder {
    Node<JCIdent> build(String name);
    Node<JCIdent> build(Name name);
}
