package kendal.api.impl.builders;

import java.util.Collections;
import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.TreeMaker;

import kendal.api.AstUtils;
import kendal.api.builders.MethodInvocationBuilder;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class MethodInvocationBuilderImpl extends AbstractBuilder implements MethodInvocationBuilder {

    public MethodInvocationBuilderImpl(AstUtils astUtils, TreeMaker treeMaker) {
        super(astUtils, treeMaker);
    }

    @Override
    public <T extends JCExpression> Node<JCMethodInvocation> build(Node<T> method) {
        return build(method, com.sun.tools.javac.util.List.<JCExpression>nil());
    }

    @Override
    public <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> build(Node<T> method,
            List<Node<P>> parameters) {
        return build(method, astUtils.mapNodesToJCListOfObjects(parameters));
    }

    @Override
    public <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> build(Node<T> method,
            Node<P> parameter) {
        return build(method, Collections.singletonList(parameter));
    }

    @Override
    public <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> build(Node<T> method,
            com.sun.tools.javac.util.List<P> parameters) {
        JCMethodInvocation jcMethodInvocation = treeMaker.App(method.getObject(),
                (com.sun.tools.javac.util.List<JCExpression>) parameters);
        return TreeBuilder.buildNode(jcMethodInvocation);
    }
}
