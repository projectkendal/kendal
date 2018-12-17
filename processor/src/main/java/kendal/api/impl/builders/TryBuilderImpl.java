package kendal.api.impl.builders;

import java.util.Collections;
import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCTry;
import com.sun.tools.javac.tree.TreeMaker;

import kendal.api.AstUtils;
import kendal.api.builders.TryBuilder;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class TryBuilderImpl implements TryBuilder {

    private final AstUtils astUtils;
    private final TreeMaker treeMaker;

    public TryBuilderImpl(AstUtils astUtils, TreeMaker treeMaker) {
        this.astUtils = astUtils;
        this.treeMaker = treeMaker;
    }

    @Override
    public Node<JCTry> build(Node<JCBlock> body, Node<JCCatch> catchers) {
        return build(body, Collections.singletonList(catchers));
    }

    @Override
    public Node<JCTry> build(Node<JCBlock> body, List<Node<JCCatch>> catchers) {
        return build(body, astUtils.mapNodesToJCListOfObjects(catchers));
    }

    @Override
    public Node<JCTry> build(Node<JCBlock> body, com.sun.tools.javac.util.List<JCCatch> catchers) {
        JCTry jcTry = treeMaker.Try(body.getObject(), catchers, null);
        return TreeBuilder.buildNode(jcTry);
    }
}
