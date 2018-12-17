package kendal.api.builders;

import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;

import kendal.model.Node;

public interface MethodInvocationBuilder {
    <T extends JCExpression> Node<JCMethodInvocation> build(Node<T> method);

    <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> build(Node<T> method,
            List<Node<P>> parameters);

    <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> build(Node<T> method, Node<P> parameter);

    <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> build(Node<T> method,
            com.sun.tools.javac.util.List<P> parameters);
}
