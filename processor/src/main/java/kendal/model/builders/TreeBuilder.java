package kendal.model.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCStatement;
import com.sun.tools.javac.tree.JCTree.JCVariableDecl;
import com.sun.tools.javac.tree.JCTree.JCWhileLoop;

import kendal.model.Node;

class TreeBuilder {

    private TreeBuilder() {
        // private constructor to hide default public one
    }

    static Node buildTree(JCCompilationUnit compilationUnit) {
        return buildNode(compilationUnit);
    }

    private static Node<JCCompilationUnit> buildNode(JCCompilationUnit jcCompilationUnit) {
        return new Node<>(jcCompilationUnit, buildChildren(jcCompilationUnit));
    }

    private static Node<JCClassDecl> buildNode(JCClassDecl jcClassDecl) {
        return new Node<>(jcClassDecl, buildChildren(jcClassDecl));
    }

    private static Node<JCMethodDecl> buildNode(JCMethodDecl jcMethodDecl) {
        return new Node<>(jcMethodDecl, buildChildren(jcMethodDecl));
    }

    private static Node<JCBlock> buildNode(JCBlock jcBlock) {
        return new Node<>(jcBlock, buildChildren(jcBlock));
    }

    private static Node<JCVariableDecl> buildNode(JCVariableDecl jcVariableDecl) {
        return new Node<>(jcVariableDecl, buildChildren(jcVariableDecl));
    }

    private static Node<JCWhileLoop> buildNode(JCWhileLoop jcWhileLoop) {
        return new Node<>(jcWhileLoop, buildChildren(jcWhileLoop));
    }

    private static Node<JCDoWhileLoop> buildNode(JCDoWhileLoop jcDoWhileLoop) {
        return new Node<>(jcDoWhileLoop, buildChildren(jcDoWhileLoop));
    }

    private static Node<JCForLoop> buildNode(JCForLoop jcForLoop) {
        return new Node<>(jcForLoop, buildChildren(jcForLoop));
    }

    private static Node<JCEnhancedForLoop> buildNode(JCEnhancedForLoop jcEnhancedForLoop) {
        return new Node<>(jcEnhancedForLoop, buildChildren(jcEnhancedForLoop));
    }

    private static Node<JCImport> buildNode(JCImport jcImport) {
        return new Node<>(jcImport);
    }

    private static Node<JCAnnotation> buildNode(JCAnnotation jcAnnotation) {
        return new Node<>(jcAnnotation);
    }

    private static Node<JCExpressionStatement> buildNode(JCExpressionStatement jcExpressionStatement) {
        return new Node<>(jcExpressionStatement);
    }

    private static Node<JCStatement> buildNode(JCStatement jcStatement) {
        return new Node<>(jcStatement);
    }

    private static List<Node> buildChildren(JCCompilationUnit compilationUnit) {
        return mapChildren(def -> {
            if(def instanceof JCClassDecl) {
                return buildNode((JCClassDecl)def);
            }
            return buildNode((JCImport)def);
        }, compilationUnit.defs);
    }

    private static List<Node> buildChildren(JCClassDecl classDecl) {
        return mapChildren(def -> {
            if(def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            if(def instanceof JCMethodDecl) {
                return buildNode((JCMethodDecl) def);
            }
            if(def instanceof JCClassDecl) {
                return buildNode((JCClassDecl) def);
            }
            return buildNode((JCAnnotation) def);
        }, classDecl.defs);
    }

    private static List<Node> buildChildren(JCMethodDecl methodDecl) {
        List<Node> children = new ArrayList<>();
        methodDecl.params.forEach(param -> children.add(buildNode(param)));
        methodDecl.mods.annotations.forEach(annotation -> children.add(buildNode(annotation)));
        children.add(buildNode(methodDecl.body));
        return children;
    }

    private static List<Node> buildChildren(JCBlock block) {
        return mapChildren(def -> {
            if (def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            if (def instanceof JCWhileLoop) {
                return buildNode((JCWhileLoop) def);
            }
            if (def instanceof JCDoWhileLoop) {
                return buildNode((JCDoWhileLoop) def);
            }
            if (def instanceof JCForLoop) {
                return buildNode((JCForLoop) def);
            }
            if (def instanceof JCEnhancedForLoop) {
                return buildNode((JCEnhancedForLoop) def);
            }
            if (def instanceof JCExpressionStatement) {
                return buildNode((JCExpressionStatement) def);
            }
            return null;
        }, block.stats);
    }

    private static List<Node> buildChildren(JCVariableDecl variableDecl) {
        List<Node> children = new ArrayList<>();
        variableDecl.mods.annotations.forEach(annotation -> children.add(buildNode(annotation)));
        return children;
    }

    private static List<Node> buildChildren(JCWhileLoop loop) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static List<Node> buildChildren(JCDoWhileLoop loop) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static List<Node> buildChildren(JCForLoop loop) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static List<Node> buildChildren(JCEnhancedForLoop loop) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(loop.body));
        return children;
    }

    private static <T extends JCTree> List<Node> mapChildren(Function<T, Node> mapping, Iterable<T>... childCollections) {
        return Arrays.stream(childCollections).flatMap(c -> StreamSupport.stream(c.spliterator(), false))
                .map(mapping)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
