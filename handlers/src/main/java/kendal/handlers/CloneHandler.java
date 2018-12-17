package kendal.handlers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.lang.model.SourceVersion;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;

import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCModifiers;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTry;
import com.sun.tools.javac.tree.JCTree.JCTypeUnion;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.annotations.Clone;
import kendal.api.AstHelper;
import kendal.api.AstHelper.Mode;
import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.KendalHandler;
import kendal.api.exceptions.DuplicatedElementsException;
import kendal.api.exceptions.InvalidAnnotationParamsException;
import kendal.api.exceptions.KendalException;
import kendal.api.exceptions.KendalRuntimeException;
import kendal.model.Node;

public class CloneHandler implements KendalHandler<Clone> {

    private static final String TRANSFORM_RETURN_TYPE_NOT_FOUND = "Return type for cloned method is undefined!" +
     "This should never happen because transformer parameter, extending Clone.Transformer class is required for @Clone annotation!";

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

    /**
     * For given annotation node finds annotated method. For that method creates a new one - clone method.
     * This clone method contains transformer method invocation with initial method invocation passed as its argument.
     * Such an expression requires try-catch block around it so it is also added here.
     * When method creation is done, its added to the class where the initial method lies.
     */
    private void handleNode(Node annotationNode, AstHelper helper) throws KendalException {
        Node<JCMethodDecl> initialMethod = (Node<JCMethodDecl>) annotationNode.getParent();
        Node<JCClassDecl> clazz = (Node<JCClassDecl>) initialMethod.getParent();
        JCMethodDecl m = initialMethod.getObject();
        Name newMethodName = getNewMethodName(m.name.toString(), annotationNode.getParent());
        if(!SourceVersion.isIdentifier(newMethodName.toString())) {
            throw new InvalidAnnotationParamsException(String.format("%s is not a valid method identifier!", newMethodName.toString()));
        }
        validateMethodIsUnique(newMethodName, m.params, clazz);
        Node<JCExpression> transformerClassAccessor = getTransformerClassAccessor(initialMethod);
        Node<JCBlock> newMethodBlock = buildNewMethodBody(initialMethod, transformerClassAccessor);
        JCModifiers modifiers = getModifiersForNewMethod(m);
        JCExpression transformerReturnType = getTransformMethodReturnType(initialMethod);
        Node<JCMethodDecl> newMethod = astNodeBuilder.methodDecl().build(modifiers, newMethodName, transformerReturnType,
                m.typarams, m.params, m.thrown, newMethodBlock);
        helper.addElementToClass(clazz, newMethod, Mode.APPEND, 0);
    }

    private Node<JCBlock> buildNewMethodBody(Node<JCMethodDecl> initialMethod, Node<JCExpression> transformerClassAccessor) {
        Node<JCBlock> tryBody = buildTryBody(initialMethod, transformerClassAccessor);
        Node<JCCatch> catcher = buildCatcher(initialMethod);
        Node<JCTry> tryStatement = astNodeBuilder.buildTry(tryBody, catcher);
        return astNodeBuilder.buildBlock(tryStatement);
    }

    /**
     * Builds initial method invocation which can be then passed as an argument to the
     * {@link Clone.Transformer#transform(Object)} ()} method invocation.
     */
    private Node<JCMethodInvocation> buildInitialMethodInvocation(Node<JCMethodDecl> initialMethod) {
        Node<JCIdent> methodIdentifier = astNodeBuilder.buildIdentifier(initialMethod.getObject().name);
        methodIdentifier.getObject().setType(initialMethod.getObject().getReturnType().type);
        List<Node<JCIdent>> parametersIdentifiers = new LinkedList<>();
        initialMethod.getObject().params.forEach(param -> parametersIdentifiers.add(astNodeBuilder.buildIdentifier(param.name)));
        return astNodeBuilder.methodInvocation().build(methodIdentifier, parametersIdentifiers);
    }

    /**
     * Returns declared return type of method {@link Clone.Transformer#transform(Object)} defined by class specified
     * as {@link Clone#transformer()} for the method that is to be cloned.
     */
    private JCExpression getTransformMethodReturnType(Node<JCMethodDecl> initialMethod) {
        try {
            initialMethod.getObject().sym.getAnnotation(Clone.class).transformer();
        }
        catch (MirroredTypeException e) {
            Type.ClassType transformerClassType = (Type.ClassType) e.getTypeMirror();
            com.sun.tools.javac.util.List<Type> interfaces = transformerClassType.interfaces_field;
            Type.ClassType transformerInterface = null;
            while (transformerInterface == null) {
                // look for the first class in inheritance hierarchy which has an interface
                while (interfaces == null || interfaces.isEmpty()) {
                    transformerClassType = (Type.ClassType) transformerClassType.supertype_field;
                    interfaces = transformerClassType.interfaces_field;
                }
                transformerInterface = (Type.ClassType) interfaces.stream()
                        .filter(i -> i.tsym.getQualifiedName().contentEquals("kendal.annotations.Clone.Transformer"))
                        .findFirst().orElse(null);
                // check if one of interfaces is Clone.Transformer, if not, below is null
                if (transformerInterface == null) {
                    transformerClassType = (Type.ClassType) transformerClassType.supertype_field;
                    interfaces = transformerClassType.interfaces_field;
                }
            }
            // second type parameter of the interface represents returned type of the transform method, so we have to get(1)
            Type returnedType = transformerInterface.typarams_field.get(1);
            return astNodeBuilder.buildType(returnedType);

        }
        throw new KendalRuntimeException("Could not get transformer method identifier! This should never happen!");
    }

    /**
     * Initial method is annotated with {@link Clone} annotation. This annotation has implementation of transformer
     * class specified as its argument. This method here finds this class and returns accessor to that class.
     * Accessor can be of type either {@link Node<JCIdent>} or {@link Node<JCFieldAccess>}.
     */
    private Node<JCExpression> getTransformerClassAccessor(Node<JCMethodDecl> initialMethod) {
        try {
            initialMethod.getObject().sym.getAnnotation(Clone.class).transformer();
        }
        catch (MirroredTypeException e) {
            TypeMirror transformerClassType = e.getTypeMirror();
            return astNodeBuilder.getAccessor(transformerClassType.toString());
        }
        throw new KendalRuntimeException("Could not get transformer method identifier! This should never happen!");
    }

    private Node<JCBlock> buildTryBody(Node<JCMethodDecl> initialMethod, Node<JCExpression> transformerClassAccessor) {
        Node<JCMethodInvocation> methodInvocation = buildInitialMethodInvocation(initialMethod);
        Node<JCFieldAccess> classFieldAccess = astNodeBuilder.buildFieldAccess(transformerClassAccessor, "class");
        Node<JCFieldAccess> newInstanceFieldAccess = astNodeBuilder.buildFieldAccess(classFieldAccess, "newInstance");
        Node<JCMethodInvocation> transformerNewInstanceMethodInvocation = astNodeBuilder.methodInvocation().build(
                newInstanceFieldAccess);
        Node<JCFieldAccess> transformFieldAccess = astNodeBuilder.buildFieldAccess(transformerNewInstanceMethodInvocation, "transform");
        Node<JCMethodInvocation> transformerMethodInvocation = astNodeBuilder.methodInvocation().build(
                transformFieldAccess, methodInvocation);
        Node<JCReturn> returnStatement = astNodeBuilder.buildReturnStatement(transformerMethodInvocation);
        return astNodeBuilder.buildBlock(returnStatement);
    }

    private Node<JCCatch> buildCatcher(Node<JCMethodDecl> initialMethod) {
        String parameterName = "e";
        Node<JCVariableDecl> catcherParameter = buildCatcherParameter(parameterName, initialMethod);
        Node<JCBlock> catchBody = buildCatcherBody(parameterName);
        return astNodeBuilder.buildCatch(catcherParameter, catchBody);
    }

    private Node<JCVariableDecl> buildCatcherParameter(String parameterName, Node<JCMethodDecl> initialMethod) {
        Node<JCIdent> type1 = astNodeBuilder.buildIdentifier("InstantiationException");
        Node<JCIdent> type2 = astNodeBuilder.buildIdentifier("IllegalAccessException");
        Node<JCTypeUnion> typeUnion = astNodeBuilder.buildTypeUnion(Arrays.asList(type1, type2));
        return astNodeBuilder.variableDecl().build(typeUnion, parameterName, initialMethod);
    }

    private Node<JCBlock> buildCatcherBody(String parameterName) {
        Node<JCIdent> parameterIdentifier = astNodeBuilder.buildIdentifier(parameterName);
        Node<JCIdent> clazzIdentifier = astNodeBuilder.buildIdentifier("RuntimeException");
        Node<JCNewClass> newClassStatement = astNodeBuilder.buildNewClass(clazzIdentifier, parameterIdentifier);
        Node<JCThrow> throwStatement = astNodeBuilder.buildThrow(newClassStatement);
        return astNodeBuilder.buildBlock(throwStatement);
    }

    private Name getNewMethodName(String originMethodName, Node<JCMethodDecl> newdMethod) {
        String proposedName = newdMethod.getObject().sym.getAnnotation(Clone.class).methodName();
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

    /**
     * Check whether two collections of parameters are equal comparing their names and declared types.
     */
    private boolean collectionsOfParametersEqualByValues(List<JCVariableDecl> params1, List<JCVariableDecl> params2) {
        if (params1.size() != params2.size()) return false;

        return params1.stream().allMatch(p1 ->
                params2.stream().anyMatch(p2 ->
                        p1.name == p2.name && Objects.equals(p1.vartype.toString(), p2.vartype.toString())
                )
        );
    }

    /**
     * Creates modifiers definition for new method to be created. Those modifiers are the same as for the source
     * method except for annotations.
     */
    private JCModifiers getModifiersForNewMethod(JCMethodDecl methodDecl) {
        JCModifiers newModifiers = (JCModifiers) methodDecl.mods.clone();
        // Reset annotations
        newModifiers.annotations = astUtils.toJCList(new ArrayList<>());
        // todo: add annotations based on @Clone annotation parameter https://trello.com/c/ec4NE8Eb/30-clone-add-possibility-to-put-annotations-on-newly-created-method
        return newModifiers;
    }
}
