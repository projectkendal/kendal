package kendal.api.builders;

import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCNewClass;

import kendal.model.Node;

public interface NewClassBuilder {
    <T extends JCExpression> Node<JCNewClass> build(Node<JCIdent> clazz, Node<T> arg);
    <T extends JCExpression> Node<JCNewClass> build(Node<JCIdent> clazz, List<Node<T>> args);
}
