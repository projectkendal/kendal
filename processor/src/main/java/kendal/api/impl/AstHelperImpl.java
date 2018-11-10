package kendal.api.impl;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import kendal.api.AstHelper;
import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.AstValidator;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

public class AstHelperImpl implements AstHelper {
    private final Context context;
    private final AstUtils astUtils;
    private final AstValidator astValidator;

    public AstHelperImpl(Context context) {
        this.context = context;
        astUtils = new AstUtilsImpl(context);
        astValidator = new AstValidatorImpl(astUtils);
    }

    @Override
    public void addVariableDeclarationToClass(Node clazz, Node variableDeclaration) throws ImproperNodeTypeException {
        if (!astValidator.isClassDecl(clazz) || !astValidator.isVariableDecl(variableDeclaration)) {
            throw new ImproperNodeTypeException();
        }
        // Update Kendal AST:
        clazz.addChild(variableDeclaration);
        // Update javac AST:
        JCClassDecl classDecl = (JCClassDecl)clazz.getObject();
        JCVariableDecl variableDecl = (JCVariableDecl) variableDeclaration.getObject();
        addElementToClass(classDecl, variableDecl);
    }

    @Override
    public void addExpressionStatementToMethod(Node method, Node expressionStatement, int lineIndex)
            throws ImproperNodeTypeException {
        if (!astValidator.isMethodDecl(method) || !astValidator.isExpressionStatement(expressionStatement)) {
            throw new ImproperNodeTypeException();
        }
        // Update Kendal AST:
        method.addChild(expressionStatement);
        // Update javac AST:
        JCMethodDecl methodDecl = (JCMethodDecl) method.getObject();
    }

    private void addElementToClass(JCClassDecl classDecl, JCTree element) {
        JCTree[] newDefs = getDefinitionsArray(classDecl.defs, element);
        classDecl.defs = List.from(newDefs);
    }

    @Override
    public AstNodeBuilder getAstNodeBuilder() {
        return new AstNodeBuilderImpl(context);
    }

    @Override
    public AstValidator getAstValidator() {
        return astValidator;
    }

    private JCTree[] getDefinitionsArray(List<JCTree> defs, JCTree element) {
        JCTree[] newDefs = new JCTree[defs.size() + 1];
        for (int i = 0; i < defs.size(); i++) {
            newDefs[i] = defs.get(i);
        }
        newDefs[defs.size()] = element;
        return newDefs;
    }
}
