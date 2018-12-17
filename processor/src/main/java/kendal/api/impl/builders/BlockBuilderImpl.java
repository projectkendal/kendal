package kendal.api.impl.builders;

import java.util.Collections;
import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.TreeMaker;

import kendal.api.AstUtils;
import kendal.api.builders.BlockBuilder;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class BlockBuilderImpl implements BlockBuilder {

    private final AstUtils astUtils;
    private final TreeMaker treeMaker;

    public BlockBuilderImpl(AstUtils astUtils, TreeMaker treeMaker) {
        this.astUtils = astUtils;
        this.treeMaker = treeMaker;
    }

    @Override
    public <T extends JCStatement> Node<JCBlock> build(List<Node<T>> statements) {
        return build(astUtils.mapNodesToJCListOfObjects(statements));
    }

    @Override
    public <T extends JCStatement> Node<JCBlock> build(Node<T> statement) {
        return build(Collections.singletonList(statement));
    }

    @Override
    public <T extends JCStatement> Node<JCBlock> build(com.sun.tools.javac.util.List<T> statements) {
        JCBlock jcBlock = treeMaker.Block(0, (com.sun.tools.javac.util.List<JCStatement>) statements);
        return TreeBuilder.buildNode(jcBlock);
    }
}
