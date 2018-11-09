package kendal.handlers;

import java.util.Collection;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Name;

import kendal.annotations.PackagePrivate;
import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;
import kendal.api.AstHelper;
import kendal.api.KendalHandler;
import kendal.api.Modifier;
import kendal.exceptions.KendalRuntimeException;
import kendal.model.Node;

public abstract class TypescriptFieldsHandler<T> implements KendalHandler<T> {

    /**
     * This method assumes that all passed annotatedNodes are annotated with one of the following annotations:
     * {@link PackagePrivate}, {@link Private}, {@link Protected}, {@link Public}.
     */
    @Override
    public void handle(Collection<Node> annotatedNodes, AstHelper helper) {
        annotatedNodes.forEach(annotatedNode -> handleNode(annotatedNode, helper));
    }

    private void handleNode(Node annotatedNode, AstHelper helper) {
        Node constructorDecl = annotatedNode.getParent();
        if (!helper.getAstValidator().isConstructorDecl(constructorDecl)) {
            throw new KendalRuntimeException("Annotated element must be parameter of a constructor!");
        }
        Node classDecl = constructorDecl.getParent();
        Name name = ((JCTree.JCVariableDecl)annotatedNode.getObject()).name;
        Node newVariableDecl = helper.getAstNodeBuilder().buildVariableDecl(getModifier(), "type", name);
        helper.addVariableDeclarationToClass(classDecl, newVariableDecl);
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
