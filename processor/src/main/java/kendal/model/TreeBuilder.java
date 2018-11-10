package kendal.model;

import java.util.ArrayList;
import java.util.List;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.JCWhileLoop;

public class TreeBuilder {

    private TreeBuilder() {
        // private constructor to hide default public one
    }

    public static Node buildTree(JCCompilationUnit compilationUnit) {
        return buildNode(compilationUnit);
    }

    private static Node buildNode(JCTree jcNode) {
        return new Node(jcNode, buildChildren(jcNode));
    }

    private static List<Node> buildChildren(JCTree jcNode) {
        if (jcNode instanceof JCCompilationUnit) {
            return buildChildrenForCompilationUnit((JCCompilationUnit)jcNode);
        }
        if (jcNode instanceof JCClassDecl) {
            return buildChildrenForClassDecl((JCClassDecl)jcNode);
        }
        if (jcNode instanceof JCMethodDecl) {
            return buildChildrenForMethodDecl((JCMethodDecl)jcNode);
        }
        if (jcNode instanceof JCBlock) {
            return buildChildrenForBlock((JCBlock)jcNode);
        }
        if (jcNode instanceof JCVariableDecl) {
            return buildChildrenForVariableDecl((JCVariableDecl)jcNode);
        }
        if (jcNode instanceof JCWhileLoop) {
            return buildChildrenForWhileLoop((JCWhileLoop)jcNode);
        }
        if (jcNode instanceof JCDoWhileLoop) {
            return buildChildrenForDoWhileLoop((JCDoWhileLoop)jcNode);
        }
        if (jcNode instanceof JCForLoop) {
            return buildChildrenForForLoop((JCForLoop)jcNode);
        }
        if (jcNode instanceof JCEnhancedForLoop) {
            return buildChildrenForEnhancedForLoop((JCEnhancedForLoop)jcNode);
        }
        return new ArrayList<>();
    }

    private static List<Node> buildChildrenForCompilationUnit(JCCompilationUnit compilationUnit) {
        List<Node> children = new ArrayList<>();
        compilationUnit.defs.forEach(def -> children.add(buildNode(def)));
        return children;
    }

    private static List<Node> buildChildrenForClassDecl(JCClassDecl classDecl) {
        List<Node> children = new ArrayList<>();
        classDecl.defs.forEach(def -> children.add(buildNode(def)));
        return children;
    }

    private static List<Node> buildChildrenForMethodDecl(JCMethodDecl methodDecl) {
        List<Node> children = new ArrayList<>();
        methodDecl.params.forEach(param -> children.add(buildNode(param)));
        methodDecl.mods.annotations.forEach(annotation -> children.add(buildNode(annotation)));
        children.add(buildNode(methodDecl.body));
        return children;
    }

    private static List<Node> buildChildrenForBlock(JCBlock block) {
        List<Node> children = new ArrayList<>();
        block.stats.forEach(statement -> children.add(buildNode(statement)));
        return children;
    }

    private static List<Node> buildChildrenForVariableDecl(JCVariableDecl variableDecl) {
        List<Node> children = new ArrayList<>();
        variableDecl.mods.annotations.forEach(annotation -> children.add(buildNode(annotation)));
        return children;
    }

    private static List<Node> buildChildrenForWhileLoop(JCWhileLoop loop) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static List<Node> buildChildrenForDoWhileLoop(JCDoWhileLoop loop) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static List<Node> buildChildrenForForLoop(JCForLoop loop) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static List<Node> buildChildrenForEnhancedForLoop(JCEnhancedForLoop loop) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(loop.body));
        return children;
    }
}
