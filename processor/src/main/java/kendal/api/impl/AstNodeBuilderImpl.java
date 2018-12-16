package kendal.api.impl;

import static kendal.utils.Utils.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTry;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;

import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.AstValidator;
import kendal.api.Modifier;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class AstNodeBuilderImpl implements AstNodeBuilder {
    private static final JCExpression NO_VALUE = null;

    private final TreeMaker treeMaker;
    private AstValidator astValidator;
    private AstUtils astUtils;

    AstNodeBuilderImpl(Context context, AstUtils astUtils, AstValidator astValidator) {
        this.treeMaker = TreeMaker.instance(context);
        this.astValidator = astValidator;
        this.astUtils = astUtils;
    }

    @Override
    public <T extends JCExpression> Node<JCVariableDecl> buildVariableDecl(Node<T> type, String name) {
        return buildVariableDecl(type, astUtils.nameFromString(name));
    }

    @Override
    public <T extends JCExpression> Node<JCVariableDecl> buildVariableDecl(Node<T> type, Name name) {
        return buildVariableDecl(new LinkedList<>(), type.getObject(), name, null);
    }

    @Override
    public <T extends JCExpression, K extends JCTree> Node<JCVariableDecl> buildVariableDecl(Node<T> type, String name,
            Node<K> source) {
        return buildVariableDecl(new LinkedList<>(), type.getObject(), astUtils.nameFromString(name), source);
    }

    @Override
    public <T extends JCExpression, K extends JCTree> Node<JCVariableDecl> buildVariableDecl(List<Modifier> modifiers, T type,
            Name name, Node<K> source) {
        JCModifiers jcModifiers = treeMaker.Modifiers(map(modifiers, (List<Modifier>m) -> {
            long result = 0;
            for (Modifier mod : m) {
                result |= mod.getFlag();
            }
            return result;
        }));
        if (source != null) treeMaker.at(source.getObject().pos);
        JCVariableDecl jcVariableDecl = treeMaker.VarDef(jcModifiers, name, type, NO_VALUE);
        return TreeBuilder.buildNode(jcVariableDecl);
    }

    @Override
    public Node<JCMethodDecl> buildMethodDecl(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            Node<JCBlock> body) {
        return buildMethodDecl(modifiers, name, resType, params, thrown, body.getObject());
    }

    @Override
    public Node<JCMethodDecl> buildMethodDecl(JCModifiers jcModifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            JCBlock body) {
        // todo: add support for typarams and thrown https://trello.com/c/6CwfuLUH/46-astnodebuilder-typarams-and-thrown-support
        com.sun.tools.javac.util.List<JCTypeParameter> typarams = astUtils.toJCList(new ArrayList<>());
        JCMethodDecl jcMethodDecl = treeMaker.MethodDef(jcModifiers, name, resType, typarams, params, thrown,
                body, null);
        return TreeBuilder.buildNode(jcMethodDecl);
    }

    @Override
    public <T extends JCExpression> Node<JCMethodInvocation> buildMethodInvocation(Node<T> method) {
        return buildMethodInvocation(method, com.sun.tools.javac.util.List.<JCExpression>nil());
    }

    @Override
    public <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation>
    buildMethodInvocation(Node<T> method, List<Node<P>> parameters) {
        return buildMethodInvocation(method, astUtils.mapNodesToJCListOfObjects(parameters));
    }

    @Override
    public <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> buildMethodInvocation(
            Node<T> method, Node<P> parameter) {
        return buildMethodInvocation(method, Collections.singletonList(parameter));
    }

    @Override
    public <T extends JCExpression, P extends JCExpression> Node<JCMethodInvocation> buildMethodInvocation(Node<T> method,
            com.sun.tools.javac.util.List<P> parameters) {
        try {
            JCMethodInvocation jcMethodInvocation = treeMaker.App(method.getObject(),
                    (com.sun.tools.javac.util.List<JCExpression>) parameters);
            return TreeBuilder.buildNode(jcMethodInvocation);
        } catch (RuntimeException ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public <T extends JCExpression> Node<JCFieldAccess> buildFieldAccess(Node<T> objectRef, String fieldName) {
        return buildFieldAccess(objectRef, astUtils.nameFromString(fieldName));
    }

    @Override
    public <T extends JCExpression> Node<JCFieldAccess> buildFieldAccess(Node<T> objectRef, Name fieldName) {
        JCFieldAccess jcFieldAccess = treeMaker.Select(objectRef.getObject(), fieldName);
        jcFieldAccess.setType(Type.noType);
        return TreeBuilder.buildNode(jcFieldAccess);
    }

    public Node<JCExpression> getAccessor(String fullName) {
        List<String> elements = Arrays.asList(fullName.split("\\."));

        Node<?> result = null;
        for (String elem : elements) {
            if (result == null) result = buildIdentifier(elem);
            else result = buildFieldAccess((Node<JCExpression>) result, elem);
        }
        return (Node<JCExpression>) result;
    }

    @Override
    public <T extends JCExpression> Node<JCReturn> buildReturnStatement(Node<T> expression) {
        JCReturn jcReturn = treeMaker.Return(expression.getObject());
        return TreeBuilder.buildNode(jcReturn);
    }

    @Override
    public <T extends JCStatement> Node<JCBlock> buildBlock(List<Node<T>> statements) {
        return buildBlock(astUtils.mapNodesToJCListOfObjects(statements));
    }

    @Override
    public <T extends JCStatement> Node<JCBlock> buildBlock(Node<T> statement) {
        return buildBlock(Collections.singletonList(statement));
    }

    @Override
    public <T extends JCStatement> Node<JCBlock> buildBlock(com.sun.tools.javac.util.List<T> statements) {
        JCBlock jcBlock = treeMaker.Block(0, (com.sun.tools.javac.util.List<JCStatement>) statements);
        return TreeBuilder.buildNode(jcBlock);
    }

    @Override
    public Node<JCIdent> buildIdentifier(String name) {
        return buildIdentifier(astUtils.nameFromString(name));
    }

    @Override
    public Node<JCIdent> buildIdentifier(Name name) {
        JCIdent jcIdentifier = treeMaker.Ident(name);
        return TreeBuilder.buildNode(jcIdentifier);
    }

    @Override
    public Node<JCTry> buildTry(Node<JCBlock> body, Node<JCCatch> catchers) {
        return buildTry(body, Collections.singletonList(catchers));
    }

    @Override
    public Node<JCTry> buildTry(Node<JCBlock> body, List<Node<JCCatch>> catchers) {
        return buildTry(body, astUtils.mapNodesToJCListOfObjects(catchers));
    }

    @Override
    public Node<JCTry> buildTry(Node<JCBlock> body, com.sun.tools.javac.util.List<JCCatch> catchers) {
        JCTry jcTry = treeMaker.Try(body.getObject(), catchers, null);
        return TreeBuilder.buildNode(jcTry);
    }

    @Override
    public Node<JCCatch> buildCatch(Node<JCVariableDecl> param, Node<JCBlock> body) {
        JCCatch jcCatch = treeMaker.Catch(param.getObject(), body.getObject());
        return TreeBuilder.buildNode(jcCatch);
    }

    @Override
    public <T extends JCExpression> Node<JCThrow> buildThrow(Node<T> expression) {
        JCThrow jcThrow = treeMaker.Throw(expression.getObject());
        return TreeBuilder.buildNode(jcThrow);
    }

    @Override
    public <T extends JCExpression> Node<JCNewClass> buildNewClass(Node<JCIdent> clazz, Node<T> arg) {
        return buildNewClass(clazz, Collections.singletonList(arg));
    }

    @Override
    public <T extends JCExpression> Node<JCNewClass> buildNewClass(Node<JCIdent> clazz, List<Node<T>> args) {
        com.sun.tools.javac.util.List jcArgs = astUtils.mapNodesToJCListOfObjects(args);
        JCNewClass jcNewClass = treeMaker.NewClass(null, com.sun.tools.javac.util.List.nil(),
                clazz.getObject(), jcArgs, null);
        return TreeBuilder.buildNode(jcNewClass);
    }

    @Override
    public <T extends JCExpression> Node<JCTypeUnion> buildTypeUnion(List<Node<T>> components) {
        JCTypeUnion jcTypeUnion =
                treeMaker.TypeUnion((com.sun.tools.javac.util.List<JCExpression>) astUtils.mapNodesToJCListOfObjects(components));
        return TreeBuilder.buildNode(jcTypeUnion);
    }

    @Override
    public JCExpression buildType(Type type) {
        return treeMaker.Type(type);
    }

    @Override
    public JCBinary buildBinary(JCTree.Tag opcode, JCExpression lhs, JCExpression rhs) {
        return treeMaker.Binary(opcode, lhs, rhs);
    }

    @Override
    public JCLiteral buildLiteral(String value) {
        return treeMaker.Literal(value);
    }

    @Override
    public <L extends JCExpression, R extends JCExpression> Node<JCExpressionStatement>
    buildAssignmentStatement(Node<L> lhs, Node<R> rhs) throws ImproperNodeTypeException {
        if (!astValidator.isExpression(lhs) || !astValidator.isExpression(rhs)) {
            throw new ImproperNodeTypeException();
        }
        JCExpressionStatement jcExpressionStatement = treeMaker.Exec(treeMaker.Assign(lhs.getObject(), rhs.getObject()));
        return TreeBuilder.buildNode(jcExpressionStatement);
    }

}
