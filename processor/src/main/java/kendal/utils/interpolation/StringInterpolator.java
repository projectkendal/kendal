package kendal.utils.interpolation;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCUnary;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;

import kendal.api.exceptions.KendalRuntimeException;
import kendal.model.Node;
import kendal.utils.ForestUtils;
import kendal.utils.KendalMessager;

public class StringInterpolator {

    private final ParserFactory parserFactory;
    private final TreeMaker treeMaker;
    private final KendalMessager messager;

    public StringInterpolator(Context context, KendalMessager messager) {
        this.parserFactory = ParserFactory.instance(context);
        this.treeMaker = TreeMaker.instance(context);
        this.messager = messager;
    }

    public void interpolate(Set<Node> nodes) {
        long startTime = System.currentTimeMillis();
        ForestUtils.traverse(nodes, node -> {
            if (node.getObject() instanceof JCUnary
                    && node.getObject().getTag().equals(JCTree.Tag.POS) // unary +
                    && ((JCUnary) node.getObject()).arg instanceof JCLiteral
                    && ((JCLiteral) ((JCUnary) node.getObject()).arg).value instanceof String) {

                String literal = (String) ((JCLiteral) ((JCUnary) node.getObject()).arg).value;
                List<String> split = splitLiteral(literal);
                List<JCExpression> expressions = buildExpressions(split);

                JCExpression result = expressions.get(0);
                expressions.remove(0);
                while (!expressions.isEmpty()) {
                    result = treeMaker.Binary(JCTree.Tag.PLUS, result, expressions.get(0));
                    expressions.remove(0);
                }

                try {
                    replaceJCTree(node.getParent().getObject(), node.getObject(), result);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
        messager.printElapsedTime("String interpolator", startTime);
    }


    private static void replaceJCTree(JCTree parent, JCTree oldNode, JCTree newNode) throws IllegalAccessException {
        for (Field field : parent.getClass().getFields()) {
            field.setAccessible(true);
            Object obj = field.get(parent);
            if (obj == null) {
                continue;
            }
            if (obj == oldNode) {
                field.set(parent, newNode);
                return;
            }
            if (obj.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(obj); i++) {
                    if (Array.get(obj, i) == oldNode) {
                        Array.set(obj, i, newNode);
                        return;
                    }
                }
            } else if (obj instanceof com.sun.tools.javac.util.List) {
                Object[] array = ((com.sun.tools.javac.util.List) obj).toArray();
                for (int i = 0; i < array.length; i++) {
                    if (array[i] == oldNode) {
                        array[i] = newNode;
                        field.set(parent, com.sun.tools.javac.util.List.from(array));
                        return;
                    }
                }
            }
        }
        throw new KendalRuntimeException(String.format("Failed to replace child %s of %s", oldNode, parent));
    }

    private List<String> splitLiteral(String literal) {
        List<String> split = new ArrayList<>();
        while (!literal.isEmpty()) {
            int exprStart = literal.indexOf("${");
            if (exprStart != -1) {
                String preExpr = literal.substring(0, exprStart);
                if (!preExpr.isEmpty()) {
                    split.add(preExpr);
                }
                literal = literal.substring(exprStart);
                int exprEnd = literal.indexOf("}");
                if (exprEnd != -1) {
                    split.add(literal.substring(0, exprEnd + 1));
                    literal = literal.substring(exprEnd + 1);
                }
            } else {
                split.add(literal);
                break;
            }

        }
        return split;
    }

    private List<JCExpression> buildExpressions(List<String> split) {
        return split.stream().map(str -> {
            if (str.startsWith("${") && str.endsWith("}")) {
                String expr = str.substring(2, str.length() - 1);
                return parserFactory.newParser(expr, false, true, false).parseExpression();
            } else {
                return treeMaker.Literal(str);
            }
        }).collect(Collectors.toList());
    }
}
