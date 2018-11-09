package kendal.handlers;

import java.util.Collection;

import kendal.annotations.PackagePrivate;
import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;
import kendal.api.AstHelper;
import kendal.api.KendalHandler;
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
        try {
            Node constructorDecl = annotatedNode.getParent();
            if (!helper.getAstValidator().isConstructorDecl(constructorDecl)) {
                throw new KendalRuntimeException("Annotated element must be parameter of a constructor!");
            }
            Node classDecl = constructorDecl.getParent();
            Node newVariableDecl = helper.getAstNodeBuilder().buildVariableDecl("someName", "type");
            helper.addVariableDeclarationToClass(classDecl, newVariableDecl);
        }
        catch (RuntimeException ex) {
            ex.printStackTrace();
        }
    }

    public static class PrivateHandler extends TypescriptFieldsHandler<Private> {

        @Override
        public Class getHandledAnnotationType() {
            return Private.class;
        }
    }

    public static class ProtectedHandler extends TypescriptFieldsHandler<Protected> {

        @Override
        public Class getHandledAnnotationType() {
            return Protected.class;
        }
    }

    public static class PackagePrivateHandler extends TypescriptFieldsHandler<PackagePrivate> {

        @Override
        public Class getHandledAnnotationType() {
            return PackagePrivate.class;
        }
    }

    public static class PublicHandler extends TypescriptFieldsHandler<Public> {

        @Override
        public Class getHandledAnnotationType() {
            return Public.class;
        }
    }


}
