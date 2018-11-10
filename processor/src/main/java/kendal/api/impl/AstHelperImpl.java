package kendal.api.impl;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import kendal.api.AstHelper;
import kendal.api.AstNodeBuilder;
import kendal.model.Node;

public class AstHelperImpl implements AstHelper {
    private final Context context;

    public AstHelperImpl(Context context) {
        this.context = context;
    }

    @Override
    public void addVariableDeclarationToClass(Node clazz, Node variableDeclaration) {
        // Update Kendal AST:
        clazz.getChildren().add(variableDeclaration);
        // Update javac AST:
        JCClassDecl classDecl = (JCClassDecl)clazz.getObject();
        JCVariableDecl variableDecl = (JCVariableDecl) variableDeclaration.getObject();
        addElementToClass(classDecl, variableDecl);
    }

    private void addElementToClass(JCClassDecl classDecl, JCTree element) {
        JCTree[] newDefs = getDefinitionsArray(classDecl.defs, element);
        classDecl.defs = List.from(newDefs);
    }

    @Override
    public AstNodeBuilder getAstNodeBuilder() {
        return new AstNodeBuilderImpl( context);
    }

    @Override
    public AstValidatorImpl getAstValidator() {
        return new AstValidatorImpl(this);
    }

    @Override
    public Name nameFromString(String name) {
        return Names.instance(context).fromString(name);
    }

    private JCTree[] getDefinitionsArray(List<JCTree> defs, JCTree element) {
        JCTree[] newDefs = new JCTree[defs.size() + 1];
        for (int i = 0; i < defs.size(); i++) {
            newDefs[i] = defs.get(i);
        }
        newDefs[defs.size()] = element;
        return newDefs;
    }
}
