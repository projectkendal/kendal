package kendal.api.impl;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import kendal.api.AstUtils;
import kendal.api.AstValidator;
import kendal.model.Node;
import kendal.utils.Utils;

public class AstValidatorImpl implements AstValidator {

    private final AstUtils astUtils;

    AstValidatorImpl(AstUtils astUtils) {
        this.astUtils = astUtils;
    }

    @Override
    public boolean isClass(Node node) {
        return Utils.ifNotNull(node, n -> n.getObject() instanceof JCClassDecl, false);
    }

    @Override
    public boolean isVariable(Node node) {
        return Utils.ifNotNull(node, n -> n.getObject() instanceof JCVariableDecl, false);
    }

    @Override
    public boolean isExpression(Node node) {
        return Utils.ifNotNull(node, n -> n.getObject() instanceof JCExpression, false);
    }

    @Override
    public boolean isIdent(Node node) {
        return Utils.ifNotNull(node, n -> n.getObject() instanceof JCIdent, false);
    }

    @Override
    public boolean isExpressionStatement(Node node) {
        return Utils.ifNotNull(node, n -> n.getObject() instanceof JCExpressionStatement, false);
    }

    @Override
    public boolean isMethod(Node node) {
        return Utils.ifNotNull(node, n -> n.getObject() instanceof JCMethodDecl, false);
    }

    @Override
    public boolean isConstructor(Node node) {
        boolean nameOk = ((JCMethodDecl) node.getObject()).name == astUtils.nameFromString("<init>");
        return isMethod(node) && nameOk;
    }
}
