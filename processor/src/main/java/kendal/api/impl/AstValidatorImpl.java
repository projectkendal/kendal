package kendal.api.impl;

import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import kendal.api.AstHelper;
import kendal.api.AstValidator;
import kendal.model.Node;

public class AstValidatorImpl implements AstValidator {

    private final AstHelper helper;

    AstValidatorImpl(AstHelper helper) {
        this.helper = helper;
    }

    @Override
    public boolean isMethodDecl(Node node) {
        return node.getObject() instanceof JCMethodDecl;
    }

    @Override
    public boolean isConstructorDecl(Node node) {
        boolean nameOk = ((JCMethodDecl) node.getObject()).name == helper.nameFromString("<init>");
        return isMethodDecl(node) && nameOk;
    }
}
