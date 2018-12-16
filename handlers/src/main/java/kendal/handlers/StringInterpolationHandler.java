package kendal.handlers;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCExpression;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCUnary;

import kendal.api.AstHelper;
import kendal.api.AstNodeBuilder;
import kendal.api.KendalHandler;
import kendal.api.exceptions.KendalRuntimeException;
import kendal.model.Node;
import kendal.model.TreeBuilder;
import kendal.utils.ForestUtils;

public class StringInterpolationHandler implements KendalHandler {

    private AstNodeBuilder astNodeBuilder;
    private ParserFactory parserFactory;
    private AstHelper astHelper;

    @Override
    public void handle(Collection annotationNodes, AstHelper helper) {
        this.astNodeBuilder = helper.getAstNodeBuilder();
        this.parserFactory = ParserFactory.instance(helper.getContext());
        this.astHelper = helper;
        interpolate(annotationNodes);
    }

    private void interpolate(Collection<Node> nodes) {
        List<Node<? extends JCTree>> interpolationTargets = new ArrayList<>();
        ForestUtils.traverse(nodes, node -> {
            if (node.getObject() instanceof JCUnary
                    && node.getObject().getTag().equals(JCTree.Tag.POS) // unary +
                    && ((JCUnary) node.getObject()).arg instanceof JCLiteral
                    && ((JCLiteral) ((JCUnary) node.getObject()).arg).value instanceof String) {
                interpolationTargets.add(node);
            }
        });
        interpolationTargets.forEach(node -> {
            String literal = (String) ((JCLiteral) ((JCUnary) node.getObject()).arg).value;
            List<String> split = splitLiteral(literal);
            List<JCExpression> expressions = buildExpressions(split);

            JCExpression result = astNodeBuilder.buildLiteral("");
            while (!expressions.isEmpty()) {
                result = astNodeBuilder.buildBinary(JCTree.Tag.PLUS, result, expressions.get(0));
                expressions.remove(0);
            }

            astHelper.replaceNode(node.getParent(), node, TreeBuilder.buildNode((JCTree.JCBinary) result));
        });
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
                return astNodeBuilder.buildLiteral(str);
            }
        }).collect(Collectors.toList());
    }

}
