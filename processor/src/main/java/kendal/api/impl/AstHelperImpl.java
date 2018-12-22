package kendal.api.impl;

import static kendal.utils.AnnotationUtils.isPutOnAnnotation;
import static kendal.utils.Utils.with;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.lang.model.element.Name;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;

import kendal.api.AstHelper;
import kendal.api.AstNodeBuilder;
import kendal.api.AstUtils;
import kendal.api.AstValidator;
import kendal.api.exceptions.ImproperNodeTypeException;
import kendal.api.exceptions.InvalidArgumentException;
import kendal.api.exceptions.KendalRuntimeException;
import kendal.model.Node;

public class AstHelperImpl implements AstHelper {
    private final Context context;
    private final AstUtils astUtils;
    private final AstValidator astValidator;
    private final AstNodeBuilder astNodeBuilder;

    public AstHelperImpl(Context context) {
        this.context = context;
        astUtils = new AstUtilsImpl(context);
        astValidator = new AstValidatorImpl(astUtils);
        astNodeBuilder = new AstNodeBuilderImpl(context, astUtils, astValidator);
    }

    @Override
    public <T extends JCTree> void addElementToClass(Node<JCClassDecl> clazz, Node<T> element, Mode mode, int offset)
            throws ImproperNodeTypeException {
        if (!astValidator.isClass(clazz)) {
            throw new ImproperNodeTypeException();
        }
        // Update Kendal AST:
        clazz.addChild(element, mode, offset);

        // Update javac AST:
        JCClassDecl classDecl = clazz.getObject();
        JCTree elementDecl = element.getObject();
        if (mode == Mode.APPEND) classDecl.defs = append(classDecl.defs, elementDecl, 0);
        else classDecl.defs = prepend(classDecl.defs, elementDecl, 0);
    }

    public void replaceNode(Node<? extends JCTree> parent,
                            Node<? extends JCTree> oldNode,
                            Node<? extends JCTree> newNode) {
        // Update Kendal AST
        if (!parent.getChildren().remove(oldNode)) {
            throw new InvalidArgumentException("oldNode does not belong to children collection!");
        }
        parent.getChildren().add(newNode);

        // Update javac AST
        try {
            for (Field field : parent.getObject().getClass().getFields()) {
                field.setAccessible(true);
                Object obj = field.get(parent.getObject());
                if (obj == null) {
                    continue;
                }
                if (obj == oldNode.getObject()) {
                    field.set(parent.getObject(), newNode.getObject());
                    return;
                }
                if (obj.getClass().isArray()) {
                    for (int i = 0; i < Array.getLength(obj); i++) {
                        if (Array.get(obj, i) == oldNode.getObject()) {
                            Array.set(obj, i, newNode.getObject());
                            return;
                        }
                    }
                } else if (obj instanceof com.sun.tools.javac.util.List) {
                    Object[] array = ((com.sun.tools.javac.util.List) obj).toArray();
                    for (int i = 0; i < array.length; i++) {
                        if (array[i] == oldNode.getObject()) {
                            array[i] = newNode.getObject();
                            field.set(parent.getObject(), com.sun.tools.javac.util.List.from(array));
                            return;
                        }
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        throw new KendalRuntimeException(String.format("Failed to replace child %s of %s", oldNode.getObject(), parent.getObject()));
    }

    @Override
    public <T extends JCExpressionStatement> void addExpressionStatementToMethod(Node<JCMethodDecl> method, Node<T> expressionStatement, Mode mode, int offset) {
        commonAddExpressionStatementToMethod(method, expressionStatement, mode, offset);
    }

    @Override
    public <T extends JCExpressionStatement> void addExpressionStatementToConstructor(Node<JCMethodDecl> method,
            Node<T> expressionStatement, Mode mode, int offset) {
        Node<JCBlock> body = method.getChildrenOfType(JCBlock.class).get(0);
        if (mode == Mode.PREPEND && offset == 0 && bodyContainsSuperInvocation(body)) {
            offset++;
        }

        commonAddExpressionStatementToMethod(method, expressionStatement, mode, offset);
    }

    private <T extends JCExpressionStatement> void commonAddExpressionStatementToMethod(Node<JCMethodDecl> method,
            Node<T> expressionStatement, Mode mode, int offset) {
        // Update Kendal AST:
        Node<JCBlock> body = method.getChildrenOfType(JCBlock.class).get(0);
        body.addChild(expressionStatement, mode, offset);

        // Update javac AST:
        JCMethodDecl methodDecl = method.getObject();
        with(methodDecl.body, b -> {
            if (mode == Mode.APPEND) b.stats = append(b.stats, expressionStatement.getObject(), offset);
            else b.stats = prepend(b.stats, expressionStatement.getObject(), offset);
        });
    }

    private boolean bodyContainsSuperInvocation(Node<JCBlock> body) {
        if (!body.getChildren().isEmpty() && body.getChildren().get(0).is(JCExpressionStatement.class)) {
            JCExpressionStatement firstLineExpStat = (JCExpressionStatement) body.getChildren().get(0).getObject();
            if (firstLineExpStat.expr instanceof JCMethodInvocation) {
                JCMethodInvocation methodInv = (JCMethodInvocation) firstLineExpStat.expr;
                return methodInv.meth.toString().equals("super");
            }
            return false;
        }
        return false;
    }

    @Override
    public Node<JCVariableDecl> findFieldByNameAndType(Node<JCClassDecl> classDeclNode, Name name) {
        return classDeclNode.getChildren().stream()
                .filter(node -> node.is(JCVariableDecl.class) && ((JCVariableDecl) node.getObject()).name.equals(name))
                .map(node -> (Node<JCVariableDecl>) node)
                .findAny().orElse(null);
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public AstNodeBuilder getAstNodeBuilder() {
        return astNodeBuilder;
    }

    @Override
    public AstValidator getAstValidator() {
        return astValidator;
    }

    @Override
    public AstUtils getAstUtils() {
        return astUtils;
    }

    @Override
    public Map<Node, Node> getAnnotationSourceMap(Collection<Node> annotationNodes, String sourceQualifiedName) {
        // annotation node -> base annotation node
        Map<Node, Node> annotationToSourceMap = annotationNodes.stream()
                .map(node -> ((Node<JCAnnotation>) node))
                .collect(HashMap::new, (m,v) -> {
                    if (v.getObject().type.tsym.getQualifiedName().contentEquals(sourceQualifiedName)) {
                        m.put(v, v);
                    } else {
                        m.put(v, null);
                    }
                }, HashMap::putAll);
        // annotation name -> annotation JCClassDecl
        Map<String, Node<JCClassDecl>> annotationTypesMap = new HashMap<>();
        annotationNodes.forEach(node -> {
            if (isPutOnAnnotation(node)) {
                annotationTypesMap.put(((JCClassDecl) node.getParent().getObject()).sym.type.tsym.getQualifiedName().toString(), node.getParent());
            }
        });

        // indirect annotation name -> source annotation node
        Map<String, Node<JCAnnotation>> typesToSource = new HashMap<>();

        Set<Node> nodesWithoutSource = annotationToSourceMap.entrySet()
                .stream()
                .filter(entry -> entry.getValue() == null).map(Map.Entry::getKey)
                .collect(Collectors.toSet());
        while (!nodesWithoutSource.isEmpty()) {
            Set<Node> assignedNodes = new HashSet<>();
            nodesWithoutSource.forEach(node -> {
                String indirectAnnotationName = node.getObject().type.tsym.getQualifiedName().toString();
                Node<JCClassDecl> indirectAnnotationType = annotationTypesMap.get(indirectAnnotationName);
                indirectAnnotationType.getChildren().stream()
                        .filter(n -> n.is(JCAnnotation.class))
                        .forEach(jcAnnotationNode -> {
                    String annotationName = jcAnnotationNode.getObject().type.tsym.getQualifiedName().toString();
                    if (annotationName.equals(sourceQualifiedName)) {
                        annotationToSourceMap.put(node, jcAnnotationNode);
                        assignedNodes.add(node);
                        typesToSource.put(node.getObject().type.tsym.getQualifiedName().toString(), jcAnnotationNode);
                    } else if(typesToSource.containsKey(annotationName)) {
                        annotationToSourceMap.put(node, typesToSource.get(annotationName));
                        assignedNodes.add(node);
                        typesToSource.put(node.getObject().type.tsym.getQualifiedName().toString(), jcAnnotationNode);
                    }
                });
            });
            nodesWithoutSource.removeAll(assignedNodes);
            assignedNodes.clear();
        }

        return annotationToSourceMap;
    }

    private <T extends JCTree> List<T> append(List<T> defs, T element, int offset) {
        return add(defs.size() - offset, defs, element);
    }

    private <T extends JCTree> List<T> prepend(List<T> defs, T element, int offset) {
        return add(offset, defs, element);
    }

    private <T extends JCTree> List<T> add(int offset, List<T> defs, T element) {
        java.util.List<T> list = StreamSupport.stream(defs.spliterator(), false).collect(Collectors.toList());
        list.add(offset, element);
        return astUtils.toJCList(list);
    }

}
