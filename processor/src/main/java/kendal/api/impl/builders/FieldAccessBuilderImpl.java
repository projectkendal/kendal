package kendal.api.impl.builders;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;

import kendal.api.AstUtils;
import kendal.api.builders.FieldAccessBuilder;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class FieldAccessBuilderImpl implements FieldAccessBuilder {

    private final AstUtils astUtils;
    private final TreeMaker treeMaker;

    public FieldAccessBuilderImpl(AstUtils astUtils, TreeMaker treeMaker) {
        this.astUtils = astUtils;
        this.treeMaker = treeMaker;
    }

    @Override
    public <T extends JCExpression> Node<JCFieldAccess> build(Node<T> objectRef, String fieldName) {
        return build(objectRef, astUtils.nameFromString(fieldName));
    }

    @Override
    public <T extends JCExpression> Node<JCFieldAccess> build(Node<T> objectRef, Name fieldName) {
        JCFieldAccess jcFieldAccess = treeMaker.Select(objectRef.getObject(), fieldName);
        jcFieldAccess.setType(Type.noType);
        return TreeBuilder.buildNode(jcFieldAccess);
    }
}
