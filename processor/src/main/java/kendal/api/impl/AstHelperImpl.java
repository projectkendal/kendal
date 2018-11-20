package kendal.api.impl;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import kendal.api.AstHelper;
import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.AstValidator;
import kendal.api.exceptions.ElementNotFoundException;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static kendal.utils.Utils.with;

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
        classDecl.defs = prepend(classDecl.defs, variableDecl);
    }

    @Override
    public <T extends JCExpressionStatement> void appendExpressionStatementToMethod(Node<JCMethodDecl> method, Node<T> expressionStatement) {
        // Update Kendal AST:
        method.addChild(expressionStatement);
        // Update javac AST:
        JCMethodDecl methodDecl = method.getObject();
        with(methodDecl.body, b -> {
            b.stats = append(b.stats, expressionStatement.getObject());
        });
    }

    @Override
    public <T extends JCExpressionStatement> void prependExpressionStatementToMethod(Node<JCMethodDecl> method, Node<T> expressionStatement) {
        // Update Kendal AST:
        method.addChild(0, expressionStatement);
        // Update javac AST:
        JCMethodDecl methodDecl = method.getObject();
        with(methodDecl.body, b -> {
            b.stats = prepend(b.stats, expressionStatement.getObject());
        });
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

    private <T extends JCTree> List<T> addAfter(List<T> defs, T element, T after) {
        int index = defs.indexOf(after);
        assertFound(index);
        return add(index + 1, defs, element);
    }

    private <T extends JCTree> List<T> addBefore(List<T> defs, T element, T before) {
        int index = defs.indexOf(before);
        assertFound(index);
        return add(index + 1, defs, element);
    }

    private <T extends JCTree> List<T> append(List<T> defs, T element) {
        return add(defs.size(), defs, element);
    }

    private <T extends JCTree> List<T> prepend(List<T> defs, T element) {
        return add(0, defs, element);
    }

    private <T extends JCTree> List<T> add(int index, List<T> defs, T element) {
        java.util.List<T> list = StreamSupport.stream(defs.spliterator(), false).collect(Collectors.toList());
        list.add(index, element);
        return List.from(list);
    }

    private void assertFound(int index) {
        if(index == -1) {
            throw new ElementNotFoundException(String.format("Element %s not found in specified collection!"));
        }
    }
}
