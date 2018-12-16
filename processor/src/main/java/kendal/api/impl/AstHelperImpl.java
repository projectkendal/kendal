package kendal.api.impl;

import static kendal.utils.Utils.with;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.lang.model.element.Name;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
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
        astNodeBuilder = new AstNodeBuilderImpl(context, astUtils, astValidator);
    }

    @Override
    public <T extends JCTree> void addElementToClass(Node<JCClassDecl> clazz, Node<T> element, Mode mode, int offset)
            throws ImproperNodeTypeException {
        if (!astValidator.isClass(clazz)) {
            throw new ImproperNodeTypeException();
        }
        // Update Kendal AST:
        clazz.addChild(element, mode, offset);

        // Update javac AST:
        JCClassDecl classDecl = clazz.getObject();
        JCTree elementDecl = element.getObject();
        if (mode == Mode.APPEND) classDecl.defs = append(classDecl.defs, elementDecl, 0);
        else classDecl.defs = prepend(classDecl.defs, elementDecl, 0);
    }

    @Override
    public <T extends JCExpressionStatement> void addExpressionStatementToMethod(Node<JCMethodDecl> method, Node<T> expressionStatement, Mode mode, int offset) {
        commonAddExpressionStatementToMethod(method, expressionStatement, mode, offset);
    }

    @Override
    public <T extends JCExpressionStatement> void addExpressionStatementToConstructor(Node<JCMethodDecl> method,
            Node<T> expressionStatement, Mode mode, int offset) {
        Node<JCBlock> body = method.getChildrenOfType(JCBlock.class).get(0);
        if (mode == Mode.PREPEND && offset == 0 && bodyContainsSuperInvocation(body)) {
            offset++;
        }

        commonAddExpressionStatementToMethod(method, expressionStatement, mode, offset);
    }

    private <T extends JCExpressionStatement> void commonAddExpressionStatementToMethod(Node<JCMethodDecl> method,
            Node<T> expressionStatement, Mode mode, int offset) {
        // Update Kendal AST:
        Node<JCBlock> body = method.getChildrenOfType(JCBlock.class).get(0);
        body.addChild(expressionStatement, mode, offset);

        // Update javac AST:
        JCMethodDecl methodDecl = method.getObject();
        with(methodDecl.body, b -> {
            if (mode == Mode.APPEND) b.stats = append(b.stats, expressionStatement.getObject(), offset);
            else b.stats = prepend(b.stats, expressionStatement.getObject(), offset);
        });
    }

    private boolean bodyContainsSuperInvocation(Node<JCBlock> body) {
        if (!body.getChildren().isEmpty() && body.getChildren().get(0).getObject() instanceof JCExpressionStatement) {
            JCExpressionStatement firstLineExpStat = (JCExpressionStatement) body.getChildren().get(0).getObject();
            if (firstLineExpStat.expr instanceof JCMethodInvocation) {
                JCMethodInvocation methodInv = (JCMethodInvocation) firstLineExpStat.expr;
                return methodInv.meth.toString().equals("super");
            }
            return false;
        }
        return false;
    }

    @Override
    public Node<JCVariableDecl> findFieldByNameAndType(Node<JCClassDecl> classDeclNode, Name name) {
        return classDeclNode.getChildren().stream()
                .filter(node -> node.getObject() instanceof JCVariableDecl
                        && ((JCVariableDecl) node.getObject()).name.equals(name))
                .map(node -> (Node<JCVariableDecl>) node)
                .findAny().orElse(null);
    }

    @Override
    public Context getContext() {
        return context;
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

    private <T extends JCTree> List<T> append(List<T> defs, T element, int offset) {
        return add(defs.size() - offset, defs, element);
    }

    private <T extends JCTree> List<T> prepend(List<T> defs, T element, int offset) {
        return add(offset, defs, element);
    }

    private <T extends JCTree> List<T> add(int offset, List<T> defs, T element) {
        java.util.List<T> list = StreamSupport.stream(defs.spliterator(), false).collect(Collectors.toList());
        list.add(offset, element);
        return astUtils.toJCList(list);
    }

}
