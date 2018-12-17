package kendal.api.builders;

import java.util.List;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.api.Modifier;
import kendal.model.Node;

public interface VariableDeclBuilder {
    <T extends JCExpression> Node<JCVariableDecl> build(Node<T> type, String name);

    <T extends JCExpression> Node<JCVariableDecl> build(Node<T> type, Name name);

    <T extends JCExpression, K extends JCTree> Node<JCVariableDecl> build(Node<T> type, String name,
            Node<K> source);

    <T extends JCExpression, K extends JCTree> Node<JCVariableDecl> build(List<Modifier> modifiers, T type,
            Name name, Node<K> source);
}
