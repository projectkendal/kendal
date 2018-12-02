package kendal.api;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

/**
 * Interface for AST modification helper class
 * Instance of AstHelper is passed to implementations of {@link kendal.api.KendalHandler}
 */
public interface AstHelper {
    // MODIFICATION METHODS
    <T extends JCTree> void addElementToClass(Node<JCClassDecl> clazz, Node<T> element, Mode mode) throws ImproperNodeTypeException;
    <T extends JCExpressionStatement> void addExpressionStatementToMethod(Node<JCMethodDecl> method,
            Node<T> expressionStatement, Mode mode) throws ImproperNodeTypeException;

    // SPECIFIC HELPERS
    AstNodeBuilder getAstNodeBuilder();
    AstValidator getAstValidator();
    AstUtils getAstUtils();

    enum Mode {
        APPEND, PREPEND
    }
}
