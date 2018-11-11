package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class EnhancedForLoopNode extends Node<JCTree.JCEnhancedForLoop> {

    public EnhancedForLoopNode(JCTree.JCEnhancedForLoop object) {
        super(object);
    }

    public EnhancedForLoopNode(JCTree.JCEnhancedForLoop object, List<Node> children) {
        super(object, children);
    }

    public EnhancedForLoopNode(JCTree.JCEnhancedForLoop object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public EnhancedForLoopNode(JCTree.JCEnhancedForLoop object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
