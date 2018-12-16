package kendal.api;

import javax.lang.model.element.Name;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.api.exceptions.InvalidArgumentException;
import kendal.model.Node;

/**
 * Interface for AST modification helper class
 * Instance of AstHelper is passed to implementations of {@link kendal.api.KendalHandler}
 */
public interface AstHelper {
    // MODIFICATION METHODS
    <T extends JCTree> void addElementToClass(Node<JCClassDecl> clazz, Node<T> element, Mode mode, int offset)
            throws ImproperNodeTypeException;
    <T extends JCExpressionStatement> void addExpressionStatementToMethod(Node<JCMethodDecl> method,
            Node<T> expressionStatement, Mode mode, int offset);
    /**
     * Does the same as {@link AstHelper#addExpressionStatementToMethod(Node, Node, Mode, int)} but takes into account
     * presence of super() invocation. If present and PREPEND mode is selected, expression statement is added after it.
     */
    <T extends JCExpressionStatement> void addExpressionStatementToConstructor(Node<JCMethodDecl> method,
            Node<T> expressionStatement, Mode mode, int offset) throws ImproperNodeTypeException;

    void replaceNode(Node<? extends JCTree> parent,
                     Node<? extends JCTree> oldNode,
                     Node<? extends JCTree> newNode);

    Node<JCVariableDecl> findFieldByNameAndType(Node<JCClassDecl> classDeclNode, Name name);

    // SPECIFIC HELPERS
    Context getContext();
    AstNodeBuilder getAstNodeBuilder();
    AstValidator getAstValidator();
    AstUtils getAstUtils();

    enum Mode {
        APPEND, PREPEND
    }
}
