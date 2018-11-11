package kendal.api.impl;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
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
    private final AstNodeBuilder astNodeBuilder;

    public AstHelperImpl(Context context) {
        this.context = context;
        astUtils = new AstUtilsImpl(context);
        astValidator = new AstValidatorImpl(astUtils);
        astNodeBuilder = new AstNodeBuilderImpl(context, astValidator);
    }

    @Override
    public void  addVariableDeclarationToClass(Node<JCClassDecl> clazz, Node<JCVariableDecl> variableDeclaration) throws ImproperNodeTypeException {
        if (!astValidator.isClass(clazz) || !astValidator.isVariable(variableDeclaration)) {
            throw new ImproperNodeTypeException();
        }
        // Update Kendal AST:
        clazz.addChild(variableDeclaration);
        // Update javac AST:
        JCClassDecl classDecl = clazz.getObject();
        JCVariableDecl variableDecl = variableDeclaration.getObject();
        addElementToClass(classDecl, variableDecl);
    }

    @Override
    public <T extends JCExpressionStatement> void addExpressionStatementToMethod(Node<JCMethodDecl> method, Node<T> expressionStatement) {
        // Update Kendal AST:
        method.addChild(expressionStatement);
        // Update javac AST:
        JCMethodDecl methodDecl = method.getObject();
        addElementToBlock(methodDecl.body, expressionStatement.getObject());
    }

    private void addElementToClass(JCClassDecl classDecl, JCTree element) {
        JCTree[] newDefs = getDefinitionsArray(classDecl.defs, element);
        classDecl.defs = List.from(newDefs);
    }

    private void addElementToBlock(JCBlock block, JCStatement element) {
        JCStatement[] newStatements = getStatementsArray(block.stats, element);
        block.stats = List.from(newStatements);
    }

    @Override
    public AstNodeBuilder getAstNodeBuilder() {
        return astNodeBuilder;
    }

    @Override
    public AstValidator getAstValidator() {
        return astValidator;
    }

    @Override
    public AstUtils getAstUtils() {
        return astUtils;
    }

    private JCTree[] getDefinitionsArray(List<JCTree> defs, JCTree element) {
        JCTree[] newDefs = new JCTree[defs.size() + 1];
        for (int i = 0; i < defs.size(); i++) {
            newDefs[i] = defs.get(i);
        }
        newDefs[defs.size()] = element;
        return newDefs;
    }

    private JCStatement[] getStatementsArray(List<JCStatement> defs, JCStatement element) {
        JCStatement[] newDefs = new JCStatement[defs.size() + 1];
        for (int i = 0; i < defs.size(); i++) {
            newDefs[i] = defs.get(i);
        }
        newDefs[defs.size()] = element;
        return newDefs;
    }
}
