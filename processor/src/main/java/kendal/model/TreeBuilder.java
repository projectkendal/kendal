package kendal.model;

import java.util.HashSet;
import java.util.Set;

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

    public static Node buildTree(JCCompilationUnit compilationUnit) {
        return buildNode(compilationUnit);
    }

    private static Node buildNode(JCTree jcNode) {
        return new Node(jcNode, buildChildren(jcNode));
    }

    private static Set<Node> buildChildren(JCTree jcNode) {
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
        return new HashSet<>();
    }

    private static Set<Node> buildChildrenForCompilationUnit(JCCompilationUnit compilationUnit) {
        Set<Node> children = new HashSet<>();
        compilationUnit.defs.forEach(def -> children.add(buildNode(def)));
        return children;
    }

    private static Set<Node> buildChildrenForClassDecl(JCClassDecl classDecl) {
        Set<Node> children = new HashSet<>();
        classDecl.defs.forEach(def -> children.add(buildNode(def)));
        return children;
    }

    private static Set<Node> buildChildrenForMethodDecl(JCMethodDecl methodDecl) {
        Set<Node> children = new HashSet<>();
        methodDecl.params.forEach(param -> children.add(buildNode(param)));
        methodDecl.mods.annotations.forEach(annotation -> children.add(buildNode(annotation)));
        children.add(buildNode(methodDecl.body));
        return children;
    }

    private static Set<Node> buildChildrenForBlock(JCBlock block) {
        Set<Node> children = new HashSet<>();
        block.stats.forEach(statement -> children.add(buildNode(statement)));
        return children;
    }

    private static Set<Node> buildChildrenForVariableDecl(JCVariableDecl variableDecl) {
        Set<Node> children = new HashSet<>();
        variableDecl.mods.annotations.forEach(annotation -> children.add(buildNode(annotation)));
        return children;
    }

    private static Set<Node> buildChildrenForWhileLoop(JCWhileLoop loop) {
        Set<Node> children = new HashSet<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static Set<Node> buildChildrenForDoWhileLoop(JCDoWhileLoop loop) {
        Set<Node> children = new HashSet<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static Set<Node> buildChildrenForForLoop(JCForLoop loop) {
        Set<Node> children = new HashSet<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static Set<Node> buildChildrenForEnhancedForLoop(JCEnhancedForLoop loop) {
        Set<Node> children = new HashSet<>();
        children.add(buildNode(loop.body));
        return children;
    }
}
