package kendal.model.builders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
import kendal.model.Node;
import kendal.model.nodes.*;

public class TreeBuilder {

    private TreeBuilder() {
        // private constructor to hide default public one
    }

    public static Node buildTree(JCCompilationUnit compilationUnit) {
        return buildNode(compilationUnit);
    }

    private static CompilationUnitNode buildNode(JCCompilationUnit jcCompilationUnit) {
        return new CompilationUnitNode(jcCompilationUnit, buildChildren(jcCompilationUnit));
    }

    private static ClassNode buildNode(JCClassDecl jcClassDecl) {
        return new ClassNode(jcClassDecl, buildChildren(jcClassDecl));
    }

    private static MethodNode buildNode(JCMethodDecl jcMethodDecl) {
        return new MethodNode(jcMethodDecl, buildChildren(jcMethodDecl));
    }

    private static BlockNode buildNode(JCBlock jcBlock) {
        return new BlockNode(jcBlock, buildChildren(jcBlock));
    }

    private static VariableDefNode buildNode(JCVariableDecl jcVariableDecl) {
        return new VariableDefNode(jcVariableDecl, buildChildren(jcVariableDecl));
    }

    private static WhileLoopNode buildNode(JCWhileLoop jcWhileLoop) {
        return new WhileLoopNode(jcWhileLoop, buildChildren(jcWhileLoop));
    }

    private static DoWhileLoopNode buildNode(JCDoWhileLoop jcDoWhileLoop) {
        return new DoWhileLoopNode(jcDoWhileLoop, buildChildren(jcDoWhileLoop));
    }

    private static ForLoopNode buildNode(JCForLoop jcForLoop) {
        return new ForLoopNode(jcForLoop, buildChildren(jcForLoop));
    }

    private static EnhancedForLoopNode buildNode(JCEnhancedForLoop jcEnhancedForLoop) {
        return new EnhancedForLoopNode(jcEnhancedForLoop, buildChildren(jcEnhancedForLoop));
    }

    private static ImportNode buildNode(JCTree.JCImport jcImport) {
        return new ImportNode(jcImport);
    }

    private static AnnotationNode buildNode(JCTree.JCAnnotation jcAnnotation) {
        return new AnnotationNode(jcAnnotation);
    }

    private static ExpressionStatementNode buildNode(JCTree.JCExpressionStatement jcExpressionStatement) {
        return new ExpressionStatementNode(jcExpressionStatement);
    }

    private static StatementNode buildNode(JCTree.JCStatement jcStatement) {
        return new StatementNode(jcStatement);
    }

    private static List<Node> buildChildren(JCCompilationUnit compilationUnit) {
        return mapChildren(def -> {
            if(def instanceof JCClassDecl) {
                return buildNode((JCClassDecl)def);
            }
            return buildNode((JCTree.JCImport)def);
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
            return buildNode((JCTree.JCAnnotation)def);
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
            if(def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            if(def instanceof JCWhileLoop) {
                return buildNode((JCWhileLoop) def);
            }
            if(def instanceof JCDoWhileLoop) {
                return buildNode((JCDoWhileLoop) def);
            }
            if(def instanceof JCForLoop) {
                return buildNode((JCForLoop) def);
            }
            if(def instanceof JCEnhancedForLoop) {
                return buildNode((JCEnhancedForLoop) def);
            }
            return buildNode((JCTree.JCExpressionStatement)def);
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
                .collect(Collectors.toList());
    }
}
