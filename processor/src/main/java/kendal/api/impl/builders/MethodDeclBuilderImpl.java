package kendal.api.impl.builders;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;

import kendal.api.AstUtils;
import kendal.api.builders.MethodDeclBuilder;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class MethodDeclBuilderImpl implements MethodDeclBuilder {

    private final AstUtils astUtils;
    private final TreeMaker treeMaker;

    public MethodDeclBuilderImpl(AstUtils astUtils, TreeMaker treeMaker) {
        this.astUtils = astUtils;
        this.treeMaker = treeMaker;
    }

    @Override
    public Node<JCMethodDecl> build(JCModifiers modifiers, String name, JCExpression resType,
            List<JCTypeParameter> typarams, List<JCVariableDecl> params, List<JCExpression> thrown,
            Node<JCBlock> body) {
        return build(modifiers, astUtils.nameFromString(name), resType, typarams, params, thrown, body);
    }

    @Override
    public Node<JCMethodDecl> build(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCTypeParameter> typarams,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            Node<JCBlock> body) {
        return build(modifiers, name, resType, typarams, params, thrown, body.getObject());
    }

    @Override
    public Node<JCMethodDecl> build(JCModifiers modifiers, String name, JCExpression resType,
            List<JCTypeParameter> typarams, List<JCVariableDecl> params, List<JCExpression> thrown, JCBlock body) {
        return build(modifiers, astUtils.nameFromString(name), resType, typarams, params, thrown, body);
    }

    @Override
    public Node<JCMethodDecl> build(JCModifiers jcModifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCTypeParameter> typarams,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            JCBlock body) {
        JCMethodDecl jcMethodDecl = treeMaker.MethodDef(jcModifiers, name, resType, typarams, params, thrown,
                body, null);
        return TreeBuilder.buildNode(jcMethodDecl);
    }
}
