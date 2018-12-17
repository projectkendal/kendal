package kendal.api.impl.builders;

import java.util.Collections;
import java.util.List;

import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.TreeMaker;

import kendal.api.AstUtils;
import kendal.api.builders.NewClassBuilder;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class NewClassBuilderImpl extends AbstractBuilder implements NewClassBuilder {

    public NewClassBuilderImpl(AstUtils astUtils, TreeMaker treeMaker) {
        super(astUtils, treeMaker);
    }

    @Override
    public <T extends JCExpression> Node<JCNewClass> build(Node<JCIdent> clazz, Node<T> arg) {
        return build(clazz, Collections.singletonList(arg));
    }

    @Override
    public <T extends JCExpression> Node<JCNewClass> build(Node<JCIdent> clazz, List<Node<T>> args) {
        com.sun.tools.javac.util.List jcArgs = astUtils.mapNodesToJCListOfObjects(args);
        JCNewClass jcNewClass = treeMaker.NewClass(null, com.sun.tools.javac.util.List.nil(),
                clazz.getObject(), jcArgs, null);
        return TreeBuilder.buildNode(jcNewClass);
    }
}
