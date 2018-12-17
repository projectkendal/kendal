package kendal.api.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.AstValidator;
import kendal.api.builders.BlockBuilder;
import kendal.api.builders.FieldAccessBuilder;
import kendal.api.builders.IdentifierBuilder;
import kendal.api.builders.MethodDeclBuilder;
import kendal.api.builders.MethodInvocationBuilder;
import kendal.api.builders.TryBuilder;
import kendal.api.builders.VariableDeclBuilder;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.api.impl.builders.BlockBuilderImpl;
import kendal.api.impl.builders.FieldAccessBuilderImpl;
import kendal.api.impl.builders.IdentifierBuilderImpl;
import kendal.api.impl.builders.MethodDeclBuilderImpl;
import kendal.api.impl.builders.MethodInvocationBuilderImpl;
import kendal.api.impl.builders.TryBuilderImpl;
import kendal.api.impl.builders.VariableDeclBuilderImpl;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class AstNodeBuilderImpl implements AstNodeBuilder {

    private final TreeMaker treeMaker;
    private final AstValidator astValidator;
    private final AstUtils astUtils;

    // builders
    private final VariableDeclBuilder variableDeclBuilder;
    private final MethodDeclBuilder methodDeclBuilder;
    private final MethodInvocationBuilder methodInvocationBuilder;
    private final FieldAccessBuilder fieldAccessBuilder;
    private final BlockBuilder blockBuilder;
    private final IdentifierBuilder identifierBuilder;
    private final TryBuilder tryBuilder;

    AstNodeBuilderImpl(Context context, AstUtils astUtils, AstValidator astValidator) {
        this.treeMaker = TreeMaker.instance(context);
        this.astValidator = astValidator;
        this.astUtils = astUtils;

        // builders
        this.variableDeclBuilder = new VariableDeclBuilderImpl(astUtils, treeMaker);
        this.methodDeclBuilder = new MethodDeclBuilderImpl(astUtils, treeMaker);
        this.methodInvocationBuilder = new MethodInvocationBuilderImpl(astUtils, treeMaker);
        this.fieldAccessBuilder = new FieldAccessBuilderImpl(astUtils, treeMaker);
        this.blockBuilder = new BlockBuilderImpl(astUtils, treeMaker);
        this.identifierBuilder = new IdentifierBuilderImpl(astUtils, treeMaker);
        this.tryBuilder = new TryBuilderImpl(astUtils, treeMaker);
    }

    @Override
    public VariableDeclBuilder variableDecl() {
        return variableDeclBuilder;
    }

    @Override
    public MethodDeclBuilder methodDecl() {
        return methodDeclBuilder;
    }

    @Override
    public MethodInvocationBuilder methodInvocation() {
        return methodInvocationBuilder;
    }

    @Override
    public FieldAccessBuilder fieldAccess() {
        return fieldAccessBuilder;
    }

    @Override
    public BlockBuilder block() {
        return blockBuilder;
    }

    @Override
    public IdentifierBuilder identifier() {
        return identifierBuilder;
    }

    @Override
    public TryBuilder tryBlock() {
        return tryBuilder;
    }

    public Node<JCExpression> getAccessor(String fullName) {
        List<String> elements = Arrays.asList(fullName.split("\\."));

        Node<?> result = null;
        for (String elem : elements) {
            if (result == null) result = identifierBuilder.build(elem);
            else result = fieldAccessBuilder.build((Node<JCExpression>) result, elem);
        }
        return (Node<JCExpression>) result;
    }

    @Override
    public <T extends JCExpression> Node<JCReturn> buildReturnStatement(Node<T> expression) {
        JCReturn jcReturn = treeMaker.Return(expression.getObject());
        return TreeBuilder.buildNode(jcReturn);
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
