package kendal.api.builders;

import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCTypeParameter;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.model.Node;

public interface MethodDeclBuilder {
    Node<JCMethodDecl> build(JCModifiers modifiers, String name, JCExpression resType,
            com.sun.tools.javac.util.List<JCTypeParameter> typarams,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            Node<JCBlock> body);

    Node<JCMethodDecl> build(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCTypeParameter> typarams,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            Node<JCBlock> body);

    Node<JCMethodDecl> build(JCModifiers modifiers, String name, JCExpression resType,
            com.sun.tools.javac.util.List<JCTypeParameter> typarams,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            JCBlock body);

    Node<JCMethodDecl> build(JCModifiers modifiers, Name name, JCExpression resType,
            com.sun.tools.javac.util.List<JCTypeParameter> typarams,
            com.sun.tools.javac.util.List<JCVariableDecl> params, com.sun.tools.javac.util.List<JCExpression> thrown,
            JCBlock body);
}
