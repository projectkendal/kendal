package kendal.api.builders;

import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCStatement;

import kendal.model.Node;

public interface BlockBuilder {
    <T extends JCStatement> Node<JCBlock> build(List<Node<T>> statements);

    <T extends JCStatement> Node<JCBlock> build(Node<T> statement);

    <T extends JCStatement> Node<JCBlock> build(com.sun.tools.javac.util.List<T> statements);
}
