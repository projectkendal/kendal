package kendal.processor;

import com.sun.tools.javac.code.Attribute;
import com.sun.tools.javac.code.Scope;
import com.sun.tools.javac.code.Symbol;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import kendal.api.AstHelper;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.api.exceptions.KendalRuntimeException;
import kendal.api.inheritance.AttrReference;
import kendal.api.inheritance.Inherit;
import kendal.model.Node;
import kendal.model.TreeBuilder;
import kendal.utils.ForestUtils;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AnnotationInheritanceHandler {

    private AstHelper astHelper;
    private TreeMaker treeMaker;

    public AnnotationInheritanceHandler(AstHelper astHelper, TreeMaker treeMaker) {
        this.astHelper = astHelper;
        this.treeMaker = treeMaker;
    }

    public void handleAnnotationInheritance(Set<Node> forest) {
        Map<String, Node<JCTree.JCClassDecl>> annotationsDeclMap = ForestUtils.getAnnotationsDeclMap(forest);
        handleInherit(forest, annotationsDeclMap);
        handleAttribute(forest, annotationsDeclMap);
        handleAttrReference(forest);
    }

    private void handleInherit(Set<Node> forest, Map<String, Node<JCTree.JCClassDecl>> annotationsDeclMap) {
        Set<Node> inherits = getAnnotationsOfType(forest, Inherit.class);
        Set<JCTree.JCClassDecl> handledAnnotations = new HashSet<>();
        inherits.forEach(node -> handleInheritingAnnotation(node.getParent(),
                handledAnnotations, annotationsDeclMap));
    }

    private void handleInheritingAnnotation(Node<JCTree.JCClassDecl> annotationDeclNode,
                                            Set<JCTree.JCClassDecl> handledNodes,
                                            Map<String, Node<JCTree.JCClassDecl>> annotationsDeclMap) {
        JCTree.JCClassDecl annotationDecl = annotationDeclNode.getObject();
        if(handledNodes.contains(annotationDecl)) {
            return;
        }
        JCTree.JCAnnotation inheritAnnotation = getJCAnnotation(annotationDecl, Inherit.class);
        if(inheritAnnotation != null) {
            Map<String, JCTree.JCMethodDecl> inheritedDefs = extractInheritedDefs(inheritAnnotation, handledNodes, annotationsDeclMap);

            addParamsToAnnotationDef(annotationDeclNode, inheritedDefs);
            storeInheritedAnnotationClass(inheritAnnotation);
            handledNodes.add(annotationDecl);
        }
    }

    private Map<String, JCTree.JCMethodDecl> extractInheritedDefs(JCTree.JCAnnotation inheritAnnotation,
                                                                  Set<JCTree.JCClassDecl> handledNodes,
                                                                  Map<String, Node<JCTree.JCClassDecl>> annotationsDeclMap) {
        String inheritedAnnotationName = ((JCTree.JCIdent) ((JCTree.JCAnnotation) ((JCTree.JCAssign) inheritAnnotation.args.head).rhs).annotationType).sym.getQualifiedName().toString();

        Map<String, JCTree.JCMethodDecl> inheritedDefs = new HashMap<>();
        if(annotationsDeclMap.containsKey(inheritedAnnotationName)) {
            // inherited annotation is part of the compilation
            Node<JCTree.JCClassDecl> inheritedAnnotation = annotationsDeclMap.get(inheritedAnnotationName);
            handleInheritingAnnotation(inheritedAnnotation, handledNodes, annotationsDeclMap);
            inheritedDefs = inheritedAnnotation.getObject().defs.stream()
                    .map(JCTree.JCMethodDecl.class::cast)
                    .collect(Collectors.toMap(h -> h.name.toString(), Function.identity()));
        } else {
            // inherited annotation has to be retrieved through ClassSymbol
            Symbol.ClassSymbol inheritedAnnotationSymbol = (Symbol.ClassSymbol) ((JCTree.JCIdent) ((JCTree.JCAnnotation) ((JCTree.JCAssign) inheritAnnotation.args.head).rhs).annotationType).sym;
            for (Scope.Entry entry = inheritedAnnotationSymbol.members().elems; entry != null; entry = entry.sibling) {
                if(entry.sym instanceof Symbol.MethodSymbol) {
                    JCTree.JCMethodDecl methodDecl = treeMaker.MethodDef((Symbol.MethodSymbol) entry.sym, null);
                    Attribute defaultValue = ((Symbol.MethodSymbol) entry.sym).defaultValue;
                    methodDecl.defaultValue = defaultValue == null ? null : treeMaker.Literal(defaultValue.getValue());
                    inheritedDefs.put(entry.sym.name.toString(), methodDecl);
                }
            }
        }

        // override default values
        for (JCTree.JCExpression arg : ((JCTree.JCAnnotation) ((JCTree.JCAssign) inheritAnnotation.args.head).rhs).args) {
            JCTree def = inheritedDefs.get(((JCTree.JCIdent) ((JCTree.JCAssign) arg).lhs).name.toString());
            if(def instanceof JCTree.JCMethodDecl) {
                ((JCTree.JCMethodDecl) def).defaultValue = ((JCTree.JCAssign) arg).rhs;
            }
        }
        return inheritedDefs;
    }

    private void addParamsToAnnotationDef(Node<JCTree.JCClassDecl> annotationDeclNode, Map<String, JCTree.JCMethodDecl> inheritedDefs) {
        inheritedDefs.values().forEach(jcMethodDecl -> {
            Node<JCTree.JCMethodDecl> newNode = TreeBuilder.buildNode(jcMethodDecl);
            try {
                astHelper.addElementToClass(annotationDeclNode, newNode);
            } catch (ImproperNodeTypeException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * Stores inherited annotation class in special metadata argument and removes any other arguments.
     * @param inheritAnnotation
     */
    private void storeInheritedAnnotationClass(JCTree.JCAnnotation inheritAnnotation) {
        inheritAnnotation.args = com.sun.tools.javac.util.List.of(
                treeMaker.Assign(
                        treeMaker.Ident(astHelper.getAstUtils().nameFromString("inheritedAnnotationClass")),
                        treeMaker.ClassLiteral(((JCTree.JCIdent) ((JCTree.JCAnnotation) ((JCTree.JCAssign) inheritAnnotation.args.head).rhs).annotationType).sym.type)));
    }

    private void handleAttribute(Set<Node> forest, Map<String, Node<JCTree.JCClassDecl>> annotationsDeclMap) {
        annotationsDeclMap.forEach((name, jcClassDeclNode) -> {
            List<Node<JCTree.JCAnnotation>> attributes = extractAttributeAnnotations(jcClassDeclNode);

            if(attributes.isEmpty()) {
                return; // no attributes to apply, so we are free
            }

            List<Node<JCTree.JCAnnotation>> annotationNodes = new ArrayList<>();
            ForestUtils.traverse(forest, node -> {
                if(node.is(JCTree.JCAnnotation.class) && node.getObject().type.tsym.getQualifiedName().contentEquals(name)) {
                    annotationNodes.add(node);
                }
            });
            annotationNodes.forEach(node -> attributes.stream().map(attr -> {
                JCTree.JCIdent attrIdent = attr.getObject().args
                        .stream()
                        .filter(arg -> ((JCTree.JCIdent) ((JCTree.JCAssign) arg).lhs).name.contentEquals("name"))
                        .map(arg -> ((JCTree.JCLiteral) ((JCTree.JCAssign) arg).rhs))
                        .map(literal -> treeMaker.Ident(astHelper.getAstUtils().nameFromString((String) literal.value)))
                        .findFirst()
                        .orElseThrow(() -> new KendalRuntimeException("Name of @Attribute is required!"));

                JCTree.JCExpression attrValue = attr.getObject().args
                        .stream()
                        .filter(arg -> ((JCTree.JCIdent) ((JCTree.JCAssign) arg).lhs).name.contentEquals("value"))
                        .map(arg -> ((JCTree.JCAssign) arg).rhs)
                        .findFirst()
                        .orElseThrow(() -> new KendalRuntimeException("Value of @Attribute is required!"));
                return astHelper.deepClone(treeMaker, treeMaker.Assign(attrIdent, attrValue));
            }).forEach(attr -> astHelper.addArgToAnnotation(node, TreeBuilder.buildNode(attr))));

            attributes.forEach(attr -> {
                attr.getObject().args = astHelper.getAstUtils().toJCList(attr.getObject().args.stream()
                        .filter(arg -> !((JCTree.JCIdent) ((JCTree.JCAssign) arg).lhs).name.contentEquals("value"))
                        .collect(Collectors.toList()));
                List<Node> children = new ArrayList<>(attr.getChildren());
                children.stream()
                        .filter(child -> (((JCTree.JCIdent) ((JCTree.JCAssign) child.getObject()).lhs).name.contentEquals("value")))
                        .forEach(attr::removeChild);
            });
        });
    }

    private List<Node<JCTree.JCAnnotation>> extractAttributeAnnotations(Node<JCTree.JCClassDecl> jcClassDeclNode) {
        List<Node<JCTree.JCAnnotation>> attributes = new ArrayList<>();

        jcClassDeclNode.getChildren().forEach(child -> {
            if(child.is(JCTree.JCAnnotation.class)) {
                if(child.getObject().type.tsym.getQualifiedName().contentEquals(kendal.api.inheritance.Attribute.class.getName())) {
                    attributes.add(child);
                }

                if(child.getObject().type.tsym.getQualifiedName().contentEquals(kendal.api.inheritance.Attribute.List.class.getName())) {

                    Node<JCTree.JCNewArray> attributeListNode = ((Node) ((Node) child.getChildren().get(0)).getChildren().get(1));
                    attributeListNode.getChildren().forEach(elem -> {
                        if(elem.is(JCTree.JCAnnotation.class)) {
                            attributes.add(elem);
                        }
                    });
                }
            }
        });
        return attributes;
    }

    private void handleAttrReference(Set<Node> forest) {
        Set<Node<JCTree.JCAnnotation>> annotationWithAttributeNodes = new HashSet<>();
        ForestUtils.traverse(forest, node -> {
            if(node.is(JCTree.JCAnnotation.class) && node.getObject().type.tsym.getAnnotation(kendal.api.inheritance.Attribute.class) != null) {
                annotationWithAttributeNodes.add(node);
            }
        });

        annotationWithAttributeNodes.forEach(node -> {
            Map<Node<JCTree.JCAnnotation>, Node<JCTree.JCAnnotation>> attrReferenceToAnnotation = new HashMap<>();
            ForestUtils.traverse(node, n -> {
                if(isAttrReferenceAnnotation(n)) {
                    attrReferenceToAnnotation.put(n, node);
                }
            });
            attrReferenceToAnnotation.forEach((attrReferenceNode, annotationNode) -> {
                String attrName = (String) ((JCTree.JCLiteral) attrReferenceNode.getObject().args.head).value;
                JCTree.JCAssign attr = (JCTree.JCAssign) annotationNode.getObject().args.stream()
                        .filter(arg -> ((JCTree.JCIdent) ((JCTree.JCAssign) arg).lhs).name.contentEquals(attrName))
                        .findFirst()
                        .orElse(Optional.ofNullable(astHelper.getAnnotationValues(annotationNode).get(attrName))
                                .map(val -> treeMaker.Literal(val))
                                .orElseThrow(() -> new KendalRuntimeException(String.format("Attribute %s on Annotation %s does not exist", attrName, annotationNode.getObject()))));

                @SuppressWarnings("OptionalGetWithoutIsPresent") Node replacementNode = TreeBuilder.buildNode(attr).getChildren().stream()
                        .filter(child -> child.getObject() == attr.rhs)
                        .findFirst().get();
                astHelper.replaceNode(attrReferenceNode.getParent(), attrReferenceNode, replacementNode);
            });
        });
    }

    /**
     *  Resolution does not work on AttrReference annotation put in semantically illegal place.
     *  We recognize {@link AttrReference} by simple name + the fact that its type is unknown
     */
    private boolean isAttrReferenceAnnotation(Node n) {
        return n.is(JCTree.JCAnnotation.class)
                && ((JCTree.JCAnnotation) n.getObject()).annotationType instanceof JCTree.JCIdent
                && ((JCTree.JCIdent) ((JCTree.JCAnnotation) n.getObject()).annotationType).name.contentEquals(AttrReference.class.getSimpleName())
                && n.getObject().type instanceof Type.UnknownType;
    }

    private JCTree.JCAnnotation getJCAnnotation(JCTree.JCClassDecl classDecl, Class<? extends Annotation> annotationClass) {
        return classDecl.mods.annotations.stream()
                .filter(ann -> ann.type.tsym.getQualifiedName().contentEquals(annotationClass.getName()))
                .findFirst()
                .orElse(null);
    }

    private Set<Node> getAnnotationsOfType(Set<Node> forest, Class annotation) {
        Set<Node> result = new HashSet<>();
        ForestUtils.traverse(forest, node -> {
            if(node.is(JCTree.JCAnnotation.class) && node.getObject().type.tsym.getQualifiedName().contentEquals(annotation.getName())) {
                result.add(node);
            }
        });
        return result;
    }
}
