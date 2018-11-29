package kendal.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.annotations.Clone;
import kendal.api.AstHelper;
import kendal.api.AstHelper.Mode;
import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.KendalHandler;
import kendal.api.exceptions.DuplicatedElementsException;
import kendal.api.exceptions.KendalException;
import kendal.model.Node;

public class CloneHandler implements KendalHandler<Clone> {

    private AstNodeBuilder astNodeBuilder;
    private AstUtils astUtils;

    @Override
    public void handle(Collection<Node> annotationNodes, AstHelper helper) throws KendalException {
        astNodeBuilder = helper.getAstNodeBuilder();
        astUtils = helper.getAstUtils();
        for (Node annotationNode : annotationNodes) {
            handleNode(annotationNode, helper);
        }
    }

    private void handleNode(Node annotationNode, AstHelper helper) throws KendalException {
        Node<JCMethodDecl> method = (Node<JCMethodDecl>) annotationNode.getParent();
        Node<JCClassDecl> clazz = (Node<JCClassDecl>) method.getParent();
        JCMethodDecl m = method.getObject();
        Name cloneMethodName = getCloneMethodName(m.name.toString(), annotationNode.getParent());
        validateMethodIsUnique(cloneMethodName, m.params, clazz);
        JCModifiers modifiers = getModifiersForNewMethod(m);
        // todo: run original method in clone method's body
//        Node<JCMethodInvocation> methodInvocation = astNodeBuilder.buildMethodInvocation(null ,null);
//        Node<JCReturn> returnStatement = astNodeBuilder.buildReturnStatement(methodInvocation);
//        Node<JCBlock> cloneMethodBlock = astNodeBuilder.buildBlock(Collections.singletonList(returnStatement));
        Node<JCMethodDecl> cloneMethod = astNodeBuilder.buildMethodDecl(modifiers, cloneMethodName, m.restype, m.params, m.body);
        helper.addElementToClass(clazz, cloneMethod, Mode.APPEND);
    }

    private Name getCloneMethodName(String originMethodName, Node<JCMethodDecl> clonedMethod) {
        String proposedName = clonedMethod.getObject().sym.getAnnotation(Clone.class).methodName();
        String newMethodName = !Objects.equals("", proposedName) ? proposedName : originMethodName + "Clone";
        return astUtils.nameFromString(newMethodName);
    }

    private void validateMethodIsUnique(Name methodName, List<JCVariableDecl> params, Node<JCClassDecl> clazz)
            throws DuplicatedElementsException {
        String name = methodName.toString();
        Set<JCMethodDecl> allMethods = clazz.getChildrenOfType(JCMethodDecl.class).stream()
                .map(Node::getObject).collect(Collectors.toSet());
        for (JCMethodDecl method : allMethods) {
            if (method.name.toString().equals(name)
                    && collectionsOfParametersEqualByValues(method.params, params)) {
                throw new DuplicatedElementsException("Clone method cannot be created because there already exists a"
                        + " method with such declaration!");
            }
        }
    }

    private boolean collectionsOfParametersEqualByValues(List<JCVariableDecl> params1, List<JCVariableDecl> params2) {
        if (params1.size() != params2.size()) return false;

        return params1.stream().allMatch(p1 ->
                params2.stream().anyMatch(p2 ->
                        p1.name == p2.name && Objects.equals(p1.vartype.toString(), p2.vartype.toString())
                )
        );
    }

    private JCModifiers getModifiersForNewMethod(JCMethodDecl methodDecl) {
        JCModifiers newModifiers = (JCModifiers) methodDecl.mods.clone();
        // Reset annotations
        newModifiers.annotations = astUtils.toJCList(new ArrayList<>());
        // todo: add annotations based on @Clone annotation parameter
        return newModifiers;
    }

    @Override
    public Class<Clone> getHandledAnnotationType() {
        return Clone.class;
    }
}
