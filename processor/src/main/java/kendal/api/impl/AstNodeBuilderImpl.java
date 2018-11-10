package kendal.api.impl;

import java.util.ArrayList;

import com.sun.tools.javac.code.TypeTag;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.Name;

import kendal.api.AstNodeBuilder;
import kendal.api.Modifier;
import kendal.model.Node;

public class AstNodeBuilderImpl implements AstNodeBuilder {
    private final static JCExpression NO_VALUE = null;

    private final TreeMaker treeMaker;

    public AstNodeBuilderImpl(Context context) {
        this.treeMaker = TreeMaker.instance(context);
    }

    @Override
    public Node buildVariableDecl(Modifier modifier, Object type, Name name) {
        JCModifiers modifiers = treeMaker.Modifiers(modifier.getFlag());
        JCExpression returnType = treeMaker.TypeIdent(TypeTag.BOOLEAN);
        JCVariableDecl variableDecl = treeMaker.VarDef(modifiers, name, returnType, NO_VALUE);
        return new Node(variableDecl, new ArrayList<>());
    }
}
