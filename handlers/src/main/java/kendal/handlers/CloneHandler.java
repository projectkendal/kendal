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

    public void handleNode(Node annotationNode, AstHelper helper) throws ImproperNodeTypeException {
        Node<JCMethodDecl> method = (Node<JCMethodDecl>) annotationNode.getParent();
        JCMethodDecl m = method.getObject();
        Node<JCClassDecl> clazz = (Node<JCClassDecl>) method.getParent();
        Name cloneMethodName = getCloneMethodName(m.name.toString(), annotationNode.getParent(), clazz);
        JCModifiers modifiers = (JCModifiers) m.mods.clone();
        // Reset annotations, todo: add annotations based on @Clone annotation parameter
        modifiers.annotations = com.sun.tools.javac.util.List.from(new ArrayList<>());
        // TODO: enhance method's body
        Node<JCMethodDecl> cloneMethod = astNodeBuilder.buildMethodDecl(modifiers, cloneMethodName, m.restype, m.params, m.body);
        helper.addElementToClass(clazz, cloneMethod, Mode.APPEND);
    }

    private Name getCloneMethodName(String originMethodName, Node<JCMethodDecl> clonedMethod, Node<JCClassDecl> clazz) {
        String proposedName = clonedMethod.getObject().sym.getAnnotation(Clone.class).methodName();
        Set<JCMethodDecl> allMethods = clazz.getChildrenOfType(JCMethodDecl.class).stream()
                .map(Node::getObject).collect(Collectors.toSet());
        String calculatedNewMethodName = Objects.equals("", proposedName) ? originMethodName + "Clone" : proposedName;
        String finalNewMethodName = calculatedNewMethodName;
        int i = 1;
        boolean methodNotUnique;
        do {
            methodNotUnique = false;
            for (JCMethodDecl method : allMethods) {
                if (method.name.toString().equals(finalNewMethodName)
                        && collectionsOfParametersEqualByValues(method.params, clonedMethod.getObject().params)) {
                    methodNotUnique = true;
                    break;
                }
            }
            if (methodNotUnique) finalNewMethodName = calculatedNewMethodName + i++;
        } while (methodNotUnique);
        return astUtils.nameFromString(finalNewMethodName);
    }

    @Override
    public Class<Clone> getHandledAnnotationType() {
        return Clone.class;
    }


    private boolean collectionsOfParametersEqualByValues(List<JCVariableDecl> params1, List<JCVariableDecl> params2) {
        if (params1.size() != params2.size()) return false;

        return params1.stream().allMatch(p1 ->
            params2.stream().anyMatch(p2 ->
                p1.name == p2.name && Objects.equals(p1.vartype.toString(), p2.vartype.toString())
            )
        );
    }
}
