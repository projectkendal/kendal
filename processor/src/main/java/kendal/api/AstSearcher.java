package kendal.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.lang.model.element.Name;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;

import kendal.model.Node;

public class AstSearcher {

    private static List<Node<JCClassDecl>> classDeclarations = new ArrayList<>();

    public static void registerNode(Node<? extends JCTree> node) {
        if (node.getObject() instanceof JCClassDecl) classDeclarations.add((Node<JCClassDecl>) node);
    }

    static public Node<JCVariableDecl> findFieldByNameAndType(Node<JCClassDecl> classDeclNode, Name name) {
        return classDeclNode.getChildren().stream()
                .filter(node -> node.getObject() instanceof JCVariableDecl
                        && ((JCVariableDecl) node.getObject()).name.equals(name))
                .findAny().orElse(null);
    }

    public static Node<JCClassDecl> getClassDeclarationByName(String fullName) {
        return classDeclarations.stream()
                .filter(classDeclaration -> Objects.equals(classDeclaration.getObject().sym.toString(), fullName))
                .findFirst()
                .orElse(null);
    }
}
