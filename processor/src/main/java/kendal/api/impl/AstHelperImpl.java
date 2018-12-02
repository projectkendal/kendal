package kendal.api.impl;

import static kendal.utils.Utils.with;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
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
        astNodeBuilder = new AstNodeBuilderImpl(context, astUtils, astValidator);
    }

    @Override
    public <T extends JCTree> void addElementToClass(Node<JCClassDecl> clazz, Node<T> element, Mode mode) throws ImproperNodeTypeException {
        if (!astValidator.isClass(clazz)) {
            throw new ImproperNodeTypeException();
        }
        // Update Kendal AST:
        if (mode == Mode.APPEND) clazz.addChild(element);
        else clazz.addChild(0, element);

        // Update javac AST:
        JCClassDecl classDecl = clazz.getObject();
        JCTree elementDecl = element.getObject();
        if (mode == Mode.APPEND) classDecl.defs = append(classDecl.defs, elementDecl);
        else classDecl.defs = prepend(classDecl.defs, elementDecl);
    }

    @Override
    public <T extends JCExpressionStatement> void addExpressionStatementToMethod(Node<JCMethodDecl> method, Node<T> expressionStatement, Mode mode) {
        // Update Kendal AST:
        if (mode == Mode.APPEND) method.addChild(expressionStatement);
        else method.addChild(0, expressionStatement);

        // Update javac AST:
        JCMethodDecl methodDecl = method.getObject();
        with(methodDecl.body, b -> {
            if (mode == Mode.APPEND) b.stats = append(b.stats, expressionStatement.getObject());
            else b.stats = prepend(b.stats, expressionStatement.getObject());
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

    private <T extends JCTree> List<T> append(List<T> defs, T element) {
        return add(defs.size(), defs, element);
    }

    private <T extends JCTree> List<T> prepend(List<T> defs, T element) {
        return add(0, defs, element);
    }

    private <T extends JCTree> List<T> add(int index, List<T> defs, T element) {
        java.util.List<T> list = StreamSupport.stream(defs.spliterator(), false).collect(Collectors.toList());
        list.add(index, element);
        return astUtils.toJCList(list);
    }

}
