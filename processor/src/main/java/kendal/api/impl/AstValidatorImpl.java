package kendal.api.impl;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import kendal.api.AstUtils;
import kendal.api.AstValidator;
import kendal.model.Node;

public class AstValidatorImpl implements AstValidator {

    private final AstUtils astUtils;

    AstValidatorImpl(AstUtils astUtils) {
        this.astUtils = astUtils;
    }

    @Override
    public boolean isClassDecl(Node node) {
        return node.getObject() instanceof JCClassDecl;
    }

    @Override
    public boolean isVariableDecl(Node node) {
        return node.getObject() instanceof JCVariableDecl;
    }

    @Override
    public boolean isExpressionStatement(Node node) {
        return node.getObject() instanceof JCExpressionStatement;
    }

    @Override
    public boolean isMethodDecl(Node node) {
        return node.getObject() instanceof JCMethodDecl;
    }

    @Override
    public boolean isConstructorDecl(Node node) {
        boolean nameOk = ((JCMethodDecl) node.getObject()).name == astUtils.nameFromString("<init>");
        return isMethodDecl(node) && nameOk;
    }
}
