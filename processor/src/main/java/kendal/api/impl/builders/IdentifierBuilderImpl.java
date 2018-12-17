package kendal.api.impl.builders;

import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;

import kendal.api.AstUtils;
import kendal.api.builders.IdentifierBuilder;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class IdentifierBuilderImpl implements IdentifierBuilder {

    private final AstUtils astUtils;
    private final TreeMaker treeMaker;

    public IdentifierBuilderImpl(AstUtils astUtils, TreeMaker treeMaker) {
        this.astUtils = astUtils;
        this.treeMaker = treeMaker;
    }

    @Override
    public Node<JCIdent> build(String name) {
        return build(astUtils.nameFromString(name));
    }

    @Override
    public Node<JCIdent> build(Name name) {
        JCIdent jcIdentifier = treeMaker.Ident(name);
        return TreeBuilder.buildNode(jcIdentifier);
    }
}
