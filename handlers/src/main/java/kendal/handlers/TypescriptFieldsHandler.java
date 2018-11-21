package kendal.handlers;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;
import com.sun.tools.javac.util.Name;
import kendal.annotations.PackagePrivate;
import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;
import kendal.api.*;
import kendal.api.exceptions.InvalidAnnotationException;
import kendal.api.exceptions.KendalException;
import kendal.model.Node;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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

        Node<JCVariableDecl> newVariable = astNodeBuilder.buildVariableDecl(modifiers, ((JCVariableDecl) annotationNode.getParent().getObject()).vartype, name, annotationNode);

        if(existingField == null) {
            helper.addVariableDeclarationToClass(clazz, newVariable);
        } else {
            if(!existingField.isAddedByKendal()) {
                throw new InvalidAnnotationException("Auto generated field was already defined manually in this class!");
            }
            if (existingField.getObject().getModifiers().flags != newVariable.getObject().getModifiers().flags) {
                throw new InvalidAnnotationException(String.format("Auto generated field %s in class %s occured more than once, with inconsistent definition!", existingField.getObject().name.toString(), clazz.getObject().name.toString()));
            }
            // here we have the case of identical field defined in more than one constructor. Let it be, skip field creation and just assign the value
        }


        Node<JCIdent> objectRef = astNodeBuilder.buildObjectReference(astUtils.nameFromString("this"));
        Node<JCFieldAccess> fieldAccess = astNodeBuilder.buildFieldAccess(objectRef, newVariable.getObject().name);
        Node<JCIdent> newVariableRef = astNodeBuilder.buildObjectReference(newVariable.getObject().name);
        Node<JCExpressionStatement> assignment = astNodeBuilder.buildAssignmentStatement(fieldAccess, newVariableRef);
        helper.prependExpressionStatementToMethod(constructor, assignment);
    }

    private List<Modifier> getModifiers(boolean finalParamValue) {
        List<Modifier> list = new ArrayList<>();
        list.add(getModifier());
        if(finalParamValue) {
            list.add(Modifier.FINAL);
        }
        return list;
    }

    private boolean getFinalParamValue(Node<JCTree.JCAnnotation> annotationNode) {
        T annotation = ((JCVariableDecl) annotationNode.getParent().getObject()).sym.getAnnotation(getHandledAnnotationType());
        annotation.annotationType();
        return getMakeFinalValue(annotation);
    }

    protected abstract boolean getMakeFinalValue(T annotation);

    abstract Modifier getModifier();
    
    public static class PrivateHandler extends TypescriptFieldsHandler<Private> {

        @Override
        public Class getHandledAnnotationType() {
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
        public Class getHandledAnnotationType() {
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
        public Class getHandledAnnotationType() {
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
        public Class getHandledAnnotationType() {
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
