package kendal.utils.interpolation;

import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.Context;
import kendal.api.exceptions.KendalRuntimeException;
import kendal.model.Node;
import kendal.utils.ForestUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static kendal.utils.Utils.with;

public class StringInterpolationUtils {

    public static void interpolate(Set<Node> nodes, Context context) {
        ForestUtils.traverse(nodes, node -> {
            if(node.getObject() instanceof JCTree.JCUnary
                    && node.getObject().getTag().equals(JCTree.Tag.POS) // unary +
                    && ((JCTree.JCUnary) node.getObject()).arg instanceof JCTree.JCLiteral
                    && ((JCTree.JCLiteral) ((JCTree.JCUnary) node.getObject()).arg).value instanceof String) {

                String literal = (String) ((JCTree.JCLiteral) ((JCTree.JCUnary) node.getObject()).arg).value;
                List<String> split = splitLiteral(literal);
                List<JCTree.JCExpression> expressions = buildExpressions(split, context);

                JCTree.JCExpression result = expressions.get(0);
                expressions.remove(0);
                while (!expressions.isEmpty()) {
                    result = TreeMaker.instance(context).Binary(JCTree.Tag.PLUS, result, expressions.get(0));
                    expressions.remove(0);
                }

                try {
                    replaceJCTree(node.getParent().getObject(), node.getObject(), result);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private static void replaceJCTree(JCTree parent, JCTree oldNode, JCTree newNode) throws IllegalAccessException {
        for (Field field : parent.getClass().getFields()) {
            field.setAccessible(true);
            Object obj = field.get(parent);
            if(obj == oldNode) {
                field.set(parent, newNode);
                return;
            }
            if(obj.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(obj); i++) {
                    if(Array.get(obj, i) == oldNode) {
                        Array.set(obj, i, newNode);
                        return;
                    }
                }
            } else if(obj instanceof com.sun.tools.javac.util.List) {
                Object[] array = ((com.sun.tools.javac.util.List) obj).toArray();
                for (int i = 0; i < array.length; i++) {
                    if(array[i] == oldNode) {
                        array[i] = newNode;
                        field.set(parent, com.sun.tools.javac.util.List.from(array));
                        return;
                    }
                }
            }
        }
        throw new KendalRuntimeException(String.format("Failed to replace child %s of %s", oldNode, parent));
    }

    private static List<String> splitLiteral(String literal) {
        List<String> split = new ArrayList<>();
        while (!literal.isEmpty()) {
            int exprStart = literal.indexOf("${");
            if(exprStart != -1) {
                String preExpr = literal.substring(0, exprStart);
                if(!preExpr.isEmpty()) {
                    split.add(preExpr);
                }
                literal = literal.substring(exprStart);
                int exprEnd = literal.indexOf("}");
                if(exprEnd != -1) {
                    split.add(literal.substring(0, exprEnd + 1));
                    literal = literal.substring(exprEnd + 1);
                }
            } else {
                split.add(literal);
                literal = "";
            }

        }
        return split;
    }

    private static List<JCTree.JCExpression> buildExpressions(List<String> split, Context context) {
        List<JCTree.JCExpression> expressions = new ArrayList<>();
        split.forEach(str -> {
            if(str.startsWith("${") && str.endsWith("}")) {
                String expr = str.substring(2, str.length() - 1);
                JCTree.JCExpression parsed = ParserFactory.instance(context).newParser(expr, false, true, false).parseExpression();
                expressions.add(parsed);
            } else {
                expressions.add(TreeMaker.instance(context).Literal(str));
            }
        });

        return expressions;
    }
}
