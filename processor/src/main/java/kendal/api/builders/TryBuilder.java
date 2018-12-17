package kendal.api.builders;

import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCTry;

import kendal.model.Node;

public interface TryBuilder {
    Node<JCTry> build(Node<JCBlock> body, Node<JCCatch> catchers);
    Node<JCTry> build(Node<JCBlock> body, List<Node<JCCatch>> catchers);
    Node<JCTry> build(Node<JCBlock> body, com.sun.tools.javac.util.List<JCCatch> catchers);
}
