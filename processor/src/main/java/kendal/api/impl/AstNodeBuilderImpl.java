package kendal.api.impl;

import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;

import kendal.api.AstNodeBuilder;
import kendal.api.AstValidator;
import kendal.api.Modifier;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;
import kendal.model.nodes.ExpressionStatementNode;
import kendal.model.nodes.FieldAccessNode;
import kendal.model.nodes.IdentifierNode;
import kendal.model.nodes.VariableDefNode;

public class AstNodeBuilderImpl implements AstNodeBuilder {
    private static final JCExpression NO_VALUE = null;

    private final TreeMaker treeMaker;
    private AstValidator astValidator;

    AstNodeBuilderImpl(Context context, AstValidator astValidator) {
        this.treeMaker = TreeMaker.instance(context);
        this.astValidator = astValidator;
    }

    @Override
    public VariableDefNode buildVariableDecl(Modifier modifier, Object type, Name name) {
        JCModifiers modifiers = treeMaker.Modifiers(modifier.getFlag());
        JCExpression returnType = treeMaker.TypeIdent(TypeTag.INT);
        JCVariableDecl variableDecl = treeMaker.VarDef(modifiers, name, returnType, NO_VALUE);
        return new VariableDefNode(variableDecl, true);
    }

    @Override
    public IdentifierNode buildObjectReference(Name fieldName) {
        JCIdent objectReference = treeMaker.Ident(fieldName);
        return new IdentifierNode(objectReference, true);
    }

    @Override
    public FieldAccessNode buildFieldAccess(IdentifierNode objectRef, Name fieldName) {
        JCFieldAccess fieldAccess = treeMaker.Select(objectRef.getObject(), fieldName);
        return new FieldAccessNode(fieldAccess, true);
    }

    @Override
    public ExpressionStatementNode buildAssignmentStatement(Node lhs, Node rhs) throws ImproperNodeTypeException {
        if (!astValidator.isExpression(lhs) || !astValidator.isExpression(rhs)) {
            throw new ImproperNodeTypeException();
        }
        JCExpressionStatement expressionStatement =
                treeMaker.Exec(treeMaker.Assign((JCExpression)lhs.getObject(), (JCExpression)rhs.getObject()));
        return new ExpressionStatementNode(expressionStatement, true);
    }
}
