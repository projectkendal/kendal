package kendal.api.impl;

import static kendal.utils.Utils.map;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;

import kendal.api.AstNodeBuilder;
import kendal.api.AstValidator;
import kendal.api.Modifier;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

public class AstNodeBuilderImpl implements AstNodeBuilder {
    private static final JCExpression NO_VALUE = null;

    private final TreeMaker treeMaker;
    private AstValidator astValidator;

    AstNodeBuilderImpl(Context context, AstValidator astValidator) {
        this.treeMaker = TreeMaker.instance(context);
        this.astValidator = astValidator;
    }

    @Override
    public Node<JCVariableDecl> buildVariableDecl(List<Modifier> modifiers, JCExpression type, Name name, Node<JCTree.JCAnnotation> source) {
        JCModifiers jcModifiers = treeMaker.Modifiers(map(modifiers, (List<Modifier>m) -> {
            long result = 0;
            for (Modifier mod : m) {
                result |= mod.getFlag();
            }
            return result;
        }));
        treeMaker.at(source.getObject().pos);
        JCVariableDecl variableDecl = treeMaker.VarDef(jcModifiers, name, type, NO_VALUE);
        return new Node<>(variableDecl, true);
    }

    @Override
    public Node<JCMethodDecl> buildMethodDecl(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCVariableDecl> params, JCBlock body) {
        // todo: add support for typarams and thrown
        com.sun.tools.javac.util.List<JCTypeParameter> typarams = com.sun.tools.javac.util.List.from(new ArrayList<>());
        com.sun.tools.javac.util.List<JCExpression> thrown = com.sun.tools.javac.util.List.from(new ArrayList<>());
        JCMethodDecl methodDecl = treeMaker.MethodDef(modifiers, name, resType, typarams, params, thrown,
                body, null);
        return new Node<>(methodDecl, true);
    }

    @Override
    public Node<JCIdent> buildObjectReference(Name fieldName) {
        JCIdent objectReference = treeMaker.Ident(fieldName);
        return new Node<>(objectReference, true);
    }

    @Override
    public Node<JCFieldAccess> buildFieldAccess(Node<JCIdent> objectRef, Name fieldName) {
        JCFieldAccess fieldAccess = treeMaker.Select(objectRef.getObject(), fieldName);
        return new Node<>(fieldAccess, true);
    }

    @Override
    public <L extends JCExpression, R extends JCExpression> Node<JCExpressionStatement>
    buildAssignmentStatement(Node<L> lhs, Node<R> rhs) throws ImproperNodeTypeException {
        if (!astValidator.isExpression(lhs) || !astValidator.isExpression(rhs)) {
            throw new ImproperNodeTypeException();
        }
        JCExpressionStatement expressionStatement =
                treeMaker.Exec(treeMaker.Assign(lhs.getObject(), rhs.getObject()));
        return new Node<>(expressionStatement, true);
    }
}
