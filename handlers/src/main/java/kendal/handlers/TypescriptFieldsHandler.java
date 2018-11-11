package kendal.handlers;

import java.util.Collection;

import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.annotations.PackagePrivate;
import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;
import kendal.api.AstHelper;
import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.KendalHandler;
import kendal.api.Modifier;
import kendal.api.exceptions.InvalidAnnotationException;
import kendal.api.exceptions.KendalException;
import kendal.model.Node;

public abstract class TypescriptFieldsHandler<T> implements KendalHandler<T> {

    private AstNodeBuilder astNodeBuilder;
    private AstUtils astUtils;

    /**
     * This method assumes that all passed annotatedNodes are annotated with one of the following annotations:
     * {@link PackagePrivate}, {@link Private}, {@link Protected}, {@link Public}.
     */
    @Override
    public void handle(Collection<Node> annotationNodes, AstHelper helper) throws KendalException {
        astNodeBuilder = helper.getAstNodeBuilder();
        astUtils = helper.getAstUtils();
        for (Node annotationNode : annotationNodes) {
            handleNode(annotationNode, helper);
        }
    }

    private void handleNode(Node annotationNode, AstHelper helper) throws KendalException {
        Node constructor = annotationNode.getParent().getParent();
        if (!helper.getAstValidator().isConstructor(constructor)) {
            throw new InvalidAnnotationException(
                    String.format("%s Annotated element must be parameter of a constructor!", annotationNode.getObject().toString()));
        }
        Node clazz = constructor.getParent();
        Name name = ((JCVariableDecl)annotationNode.getParent().getObject()).name;
        Node newVariable = astNodeBuilder.buildVariableDecl(getModifier(), "type", name);
        helper.addVariableDeclarationToClass(clazz, newVariable);
        Node objectRef = astNodeBuilder.buildObjectReference(astUtils.nameFromString("this"));
        Node fieldAccess = astNodeBuilder.buildFieldAccess(objectRef, ((JCVariableDecl) newVariable.getObject()).name);
        Node newVariableRef = astNodeBuilder.buildObjectReference(((JCVariableDecl) newVariable.getObject()).name);
        Node assignment = astNodeBuilder.buildAssignmentStatement(fieldAccess, newVariableRef);
        helper.addExpressionStatementToMethod(constructor, assignment);
    }

    abstract Modifier getModifier();

    public static class PrivateHandler extends TypescriptFieldsHandler<Private> {

        @Override
        public Class getHandledAnnotationType() {
            return Private.class;
        }

        @Override
        Modifier getModifier() {
            return Modifier.PRIVATE;
        }
    }

    public static class ProtectedHandler extends TypescriptFieldsHandler<Protected> {

        @Override
        public Class getHandledAnnotationType() {
            return Protected.class;
        }

        @Override
        Modifier getModifier() {
            return Modifier.PROTECTED;
        }
    }

    public static class PackagePrivateHandler extends TypescriptFieldsHandler<PackagePrivate> {

        @Override
        public Class getHandledAnnotationType() {
            return PackagePrivate.class;
        }

        @Override
        Modifier getModifier() {
            return Modifier.PACKAGE_PRIVATE;
        }
    }

    public static class PublicHandler extends TypescriptFieldsHandler<Public> {

        @Override
        public Class getHandledAnnotationType() {
            return Public.class;
        }

        @Override
        Modifier getModifier() {
            return Modifier.PUBLIC;
        }
    }

}
