package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class ExpressionStatementNode extends StatementNode<JCTree.JCExpressionStatement> {

    public ExpressionStatementNode(JCTree.JCExpressionStatement object) {
        super(object);
    }

    public ExpressionStatementNode(JCTree.JCExpressionStatement object, List<Node> children) {
        super(object, children);
    }

    public ExpressionStatementNode(JCTree.JCExpressionStatement object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public ExpressionStatementNode(JCTree.JCExpressionStatement object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
