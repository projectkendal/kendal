package kendal.api.impl.builders;

import static kendal.utils.Utils.map;

import java.util.LinkedList;
import java.util.List;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Name;

import kendal.api.AstUtils;
import kendal.api.Modifier;
import kendal.api.builders.VariableDeclBuilder;
import kendal.model.Node;
import kendal.model.TreeBuilder;

public class VariableDeclBuilderImpl extends AbstractBuilder implements VariableDeclBuilder {

    private static final JCExpression NO_VALUE = null;

    public VariableDeclBuilderImpl(AstUtils astUtils, TreeMaker treeMaker) {
        super(astUtils, treeMaker);
    }

    @Override
    public <T extends JCExpression> Node<JCVariableDecl> build(Node<T> type, String name) {
        return build(type, astUtils.nameFromString(name));
    }

    @Override
    public <T extends JCExpression> Node<JCVariableDecl> build(Node<T> type, Name name) {
        return build(new LinkedList<>(), type.getObject(), name, null);
    }

    @Override
    public <T extends JCExpression, K extends JCTree> Node<JCVariableDecl> build(Node<T> type, String name,
            Node<K> source) {
        return build(new LinkedList<>(), type.getObject(), astUtils.nameFromString(name), source);
    }

    @Override
    public <T extends JCExpression, K extends JCTree> Node<JCVariableDecl> build(List<Modifier> modifiers, T type,
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
}
