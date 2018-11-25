package kendal.handlers;

import java.util.ArrayList;
import java.util.Collection;

import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCAssign;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.util.Name;

import kendal.annotations.Clone;
import kendal.api.AstHelper;
import kendal.api.AstHelper.Mode;
import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.KendalHandler;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.model.Node;

public class CloneHandler implements KendalHandler<Clone> {

    private AstNodeBuilder astNodeBuilder;
    private AstUtils astUtils;

    @Override
    public void handle(Collection<Node> annotationNodes, AstHelper helper) throws ImproperNodeTypeException {
        astNodeBuilder = helper.getAstNodeBuilder();
        astUtils = helper.getAstUtils();
        for (Node annotationNode : annotationNodes) {
            handleNode(annotationNode, helper);
        }
    }

    // TODO: fix strange rename when methodName given
    public void handleNode(Node annotationNode, AstHelper helper) throws ImproperNodeTypeException {
        Node<JCMethodDecl> method = (Node<JCMethodDecl>) annotationNode.getParent();
        JCMethodDecl m = method.getObject();
        Node<JCClassDecl> clazz = (Node<JCClassDecl>) method.getParent();
        Name cloneMethodName = getCloneMethodName(m.name.toString(), annotationNode);
        JCModifiers modifiers = (JCModifiers) m.mods.clone();
        // Reset annotations, todo: add annotations based on @Clone annotation parameter
        modifiers.annotations = com.sun.tools.javac.util.List.from(new ArrayList<>());
        // TODO: enhance method's body
        Node<JCMethodDecl> cloneMethod = astNodeBuilder.buildMethodDecl(modifiers, cloneMethodName, m.restype, m.params, m.body);
        helper.addElementToClass(clazz, cloneMethod, Mode.APPEND);
    }

    private Name getCloneMethodName(String originMethodName, Node annotationNode) {
        JCAssign methodNameAssignment = (JCAssign)((JCAnnotation) annotationNode.getObject()).args.stream()
                .filter(arg -> arg instanceof JCAssign
                        && ((JCAssign) arg).lhs.toString().equals("methodName")).findFirst().orElse(null);
        // TODO: what if name is not unique?
        String name = methodNameAssignment != null ? methodNameAssignment.rhs.toString() : originMethodName + "Clone";
        return astUtils.nameFromString(name);
    }

    @Override
    public Class<Clone> getHandledAnnotationType() {
        return Clone.class;
    }
}
