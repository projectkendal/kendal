package kendal.api.exceptions;

import com.sun.tools.javac.tree.JCTree;

import kendal.api.AstHelper;
import kendal.api.KendalHandler;
import kendal.model.Node;

/**
 * Exception thrown by {@link KendalHandler} to signal misuse of {@link AstHelper} methods.
 * It means that a node that was passed to helper method represents improper type for given context.
 * Exception message is then printed out as a compilation error.
 *
 * Example: when node representing {@link JCTree.JCMethodDecl} is passed as argument for method
 * {@link AstHelper#addVariableDeclarationToClass(Node, Node)}
 */
public class ImproperNodeTypeException extends KendalException {

    public ImproperNodeTypeException() {
        super("Argument node represents object that does not match the execution context!");
    }
}
