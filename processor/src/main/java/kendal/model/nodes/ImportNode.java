package kendal.model.nodes;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import java.util.List;

public class ImportNode extends Node<JCTree.JCImport> {

    public ImportNode(JCTree.JCImport object) {
        super(object);
    }

    public ImportNode(JCTree.JCImport object, List<Node> children) {
        super(object, children);
    }

    public ImportNode(JCTree.JCImport object, boolean addedByKendal) {
        super(object, addedByKendal);
    }

    public ImportNode(JCTree.JCImport object, List<Node> children, boolean addedByKendal) {
        super(object, children, addedByKendal);
    }
}
