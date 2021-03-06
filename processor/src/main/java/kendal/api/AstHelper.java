package kendal.api;

import java.util.Collection;
import java.util.Map;

import javax.lang.model.element.Name;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

/**
 * Interface for AST modification helper class
 * Instance of AstHelper is passed to implementations of {@link kendal.api.KendalHandler}
 */
public interface AstHelper {
    // MODIFICATION METHODS
    <T extends JCTree> void addElementToClass(Node<JCClassDecl> clazz, Node<T> element, Mode mode, int offset)
            throws ImproperNodeTypeException;

    default <T extends JCTree> void addElementToClass(Node<JCClassDecl> clazz, Node<T> element)
            throws ImproperNodeTypeException {
        addElementToClass(clazz, element, Mode.APPEND, 0);
    }

    void addArgToAnnotation(Node<JCAnnotation> annotationNode, Node<JCAssign> arg);

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

    Map<Node, Node> getAnnotationSourceMap(Collection<Node> annotationNodes, String sourceQualifiedName);

    Map<String, Object> getAnnotationValues(Node<JCAnnotation> annotationNode);

    /**
     * Will perform deep clone of JCTree.
     * Method is targeted at cloning annotation parameters.
     * @param treeMaker
     * @param tree
     * @return
     */
    <T extends JCTree> T deepClone(TreeMaker treeMaker, T tree);

    enum Mode {
        APPEND, PREPEND
    }
}
