package kendal.handlers;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCFieldAccess;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Name;

import kendal.annotations.PackagePrivate;
import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;
import kendal.api.AstHelper;
import kendal.api.AstHelper.Mode;
import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.KendalHandler;
import kendal.api.Modifier;
import kendal.api.exceptions.DuplicatedElementsException;
import kendal.api.exceptions.InvalidAnnotationException;
import kendal.api.exceptions.KendalException;
import kendal.model.Node;

public abstract class TypescriptFieldsHandler<T extends Annotation> implements KendalHandler<T> {

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
        Node<JCMethodDecl> constructor = (Node<JCMethodDecl>) annotationNode.getParent().getParent();
        if (!helper.getAstValidator().isConstructor(constructor)) {
            throw new InvalidAnnotationException(
                    String.format("%s Annotated element must be parameter of a constructor!", annotationNode.getObject().toString()));
        }

        List<Modifier> modifiers = getModifiers(getFinalParamValue(annotationNode));

        Node<JCClassDecl> clazz = (Node<JCClassDecl>) constructor.getParent();
        Name name = ((JCVariableDecl)annotationNode.getParent().getObject()).name;

        Node<JCVariableDecl> existingField = helper.findFieldByName(clazz, name);

        Node<JCVariableDecl> newVariable = astNodeBuilder.buildVariableDecl(modifiers,
                ((JCVariableDecl) annotationNode.getParent().getObject()).vartype, name, annotationNode);

        if (existingField == null) {
            helper.addElementToClass(clazz, newVariable, Mode.APPEND);
        } else {
            if (!existingField.isAddedByKendal()) {
                throw new DuplicatedElementsException("Auto generated field was already defined manually in this class!");
            }
            if (existingField.getObject().getModifiers().flags != newVariable.getObject().getModifiers().flags) {
                throw new InvalidAnnotationException(String.format("Auto generated field %s in class %s occurred more than once, with inconsistent definition!",
                        existingField.getObject().name.toString(), clazz.getObject().name.toString()));
            }
            // here we have the case of identical field defined in more than one constructor. Let it be, skip field creation and just assign the value
        }


        Node<JCIdent> objectRef = astNodeBuilder.buildObjectReference(astUtils.nameFromString("this"));
        Node<JCFieldAccess> fieldAccess = astNodeBuilder.buildFieldAccess(objectRef, newVariable.getObject().name);
        Node<JCIdent> newVariableRef = astNodeBuilder.buildObjectReference(newVariable.getObject().name);
        Node<JCExpressionStatement> assignment = astNodeBuilder.buildAssignmentStatement(fieldAccess, newVariableRef);
        helper.addExpressionStatementToMethod(constructor, assignment, Mode.PREPEND);
    }

    private List<Modifier> getModifiers(boolean finalParamValue) {
        List<Modifier> list = new ArrayList<>();
        list.add(getModifier());
        if (finalParamValue) {
            list.add(Modifier.FINAL);
        }
        return list;
    }

    private boolean getFinalParamValue(Node<JCTree.JCAnnotation> annotationNode) {
        T annotation = ((JCVariableDecl) annotationNode.getParent().getObject()).sym.getAnnotation(getHandledAnnotationType());
        return getMakeFinalValue(annotation);
    }

    protected abstract boolean getMakeFinalValue(T annotation);

    abstract Modifier getModifier();
    
    public static class PrivateHandler extends TypescriptFieldsHandler<Private> {

        @Override
        public Class<Private> getHandledAnnotationType() {
            return Private.class;
        }

        @Override
        protected boolean getMakeFinalValue(Private annotation) {
            return annotation.makeFinal();
        }

        @Override
        Modifier getModifier() {
            return Modifier.PRIVATE;
        }
    }

    public static class ProtectedHandler extends TypescriptFieldsHandler<Protected> {

        @Override
        public Class<Protected> getHandledAnnotationType() {
            return Protected.class;
        }

        @Override
        protected boolean getMakeFinalValue(Protected annotation) {
            return annotation.makeFinal();
        }

        @Override
        Modifier getModifier() {
            return Modifier.PROTECTED;
        }
    }

    public static class PackagePrivateHandler extends TypescriptFieldsHandler<PackagePrivate> {

        @Override
        public Class<PackagePrivate> getHandledAnnotationType() {
            return PackagePrivate.class;
        }

        @Override
        protected boolean getMakeFinalValue(PackagePrivate annotation) {
            return annotation.makeFinal();
        }

        @Override
        Modifier getModifier() {
            return Modifier.PACKAGE_PRIVATE;
        }
    }

    public static class PublicHandler extends TypescriptFieldsHandler<Public> {

        @Override
        public Class<Public> getHandledAnnotationType() {
            return Public.class;
        }

        @Override
        protected boolean getMakeFinalValue(Public annotation) {
            return annotation.makeFinal();
        }

        @Override
        Modifier getModifier() {
            return Modifier.PUBLIC;
        }
    }

}
