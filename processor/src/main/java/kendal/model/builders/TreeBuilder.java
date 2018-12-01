package kendal.model.builders;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.JCTree.JCBinary;
import com.sun.tools.javac.tree.JCTree.JCBlock;
import com.sun.tools.javac.tree.JCTree.JCCatch;
import com.sun.tools.javac.tree.JCTree.JCClassDecl;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.tree.JCTree.JCDoWhileLoop;
import com.sun.tools.javac.tree.JCTree.JCEnhancedForLoop;
import com.sun.tools.javac.tree.JCTree.JCExpressionStatement;
import com.sun.tools.javac.tree.JCTree.JCForLoop;
import com.sun.tools.javac.tree.JCTree.JCIdent;
import com.sun.tools.javac.tree.JCTree.JCIf;
import com.sun.tools.javac.tree.JCTree.JCImport;
import com.sun.tools.javac.tree.JCTree.JCLiteral;
import com.sun.tools.javac.tree.JCTree.JCMethodDecl;
import com.sun.tools.javac.tree.JCTree.JCMethodInvocation;
import com.sun.tools.javac.tree.JCTree.JCNewClass;
import com.sun.tools.javac.tree.JCTree.JCParens;
import com.sun.tools.javac.tree.JCTree.JCReturn;
import com.sun.tools.javac.tree.JCTree.JCThrow;
import com.sun.tools.javac.tree.JCTree.JCTry;
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

    private static Node<JCMethodInvocation> buildNode(JCMethodInvocation jcMethodInvocation) {
        return new Node<>(jcMethodInvocation, buildChildren(jcMethodInvocation));
    }

    private static Node<JCBinary> buildNode(JCBinary jcBinary) {
        return new Node<>(jcBinary, buildChildren(jcBinary));
    }

    private static Node<JCIdent> buildNode(JCIdent jcIdent) {
        return new Node<>(jcIdent);
    }

    private static Node<JCLiteral> buildNode(JCLiteral jcLiteral) {
        return new Node<>(jcLiteral);
    }

    private static Node<JCTree> buildNode(JCTry jcTry) {
        return new Node<>(jcTry, buildChildren(jcTry));
    }

    private static Node<JCReturn> buildNode(JCReturn jcReturn) {
        return new Node<>(jcReturn, buildChildren(jcReturn));
    }

    private static Node<JCIf> buildNode(JCIf jcIf) {
        return new Node<>(jcIf, buildChildren(jcIf));
    }

    private static Node<JCCatch> buildNode(JCCatch jcCatch) {
        return new Node<>(jcCatch, buildChildren(jcCatch));
    }

    private static Node<JCThrow> buildNode(JCThrow jcThrow) {
        return new Node<>(jcThrow, buildChildren(jcThrow));
    }

    private static Node<JCNewClass> buildNode(JCNewClass jcNewClass) {
        return new Node<>(jcNewClass, buildChildren(jcNewClass));
    }

    private static Node<JCParens> buildNode(JCParens jcParens) {
        return new Node<>(jcParens, buildChildren(jcParens));
    }

    private static List<Node> buildChildren(JCCompilationUnit compilationUnit) {
        return mapChildren(def -> {
            if (def instanceof JCClassDecl) {
                return buildNode((JCClassDecl) def);
            }
            return buildNode((JCImport) def);
        }, compilationUnit.defs);
    }

    private static List<Node> buildChildren(JCClassDecl classDecl) {
        return mapChildren(def -> {
            if (def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            if (def instanceof JCMethodDecl) {
                return buildNode((JCMethodDecl) def);
            }
            if (def instanceof JCClassDecl) {
                return buildNode((JCClassDecl) def);
            }
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            if(def instanceof JCAnnotation) {
                return buildNode((JCAnnotation) def);
            }
            return null;
        }, classDecl.defs, classDecl.mods.annotations);

    }

    private static List<Node> buildChildren(JCMethodDecl methodDecl) {
        List<Node> children = new ArrayList<>();
        methodDecl.params.forEach(param -> children.add(buildNode((JCVariableDecl) param)));
        methodDecl.mods.annotations.forEach(annotation -> children.add(buildNode((JCAnnotation) annotation)));
        children.add(buildNode(methodDecl.body));
        return children;
    }

    private static List<Node> buildChildren(JCBlock block) {
        return mapChildren(def -> {
            if (def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            if (def instanceof JCIf) {
                return buildNode((JCIf) def);
            }
            if (def instanceof JCExpressionStatement) {
                return buildNode((JCExpressionStatement) def);
            }
            if (def instanceof JCTry) {
                return buildNode((JCTry) def);
            }
            if (def instanceof JCReturn) {
                return buildNode((JCReturn) def);
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
            if (def instanceof JCThrow) {
                return buildNode((JCThrow) def);
            }
            return null;
        }, block.stats);
    }

    private static List<Node> buildChildren(JCVariableDecl variableDecl) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if(def instanceof JCAnnotation) {
                return buildNode((JCAnnotation)def);
            }
            return null;
        }, Collections.singletonList(variableDecl.init), variableDecl.mods.annotations);
    }

    private static List<Node> buildChildren(JCWhileLoop loop) {
        List<Node> children = new ArrayList<>();
        if (loop.body instanceof JCBlock) {
            children.add(buildNode((JCBlock) loop.body));
        }
        return children;
    }

    private static List<Node> buildChildren(JCDoWhileLoop loop) {
        List<Node> children = new ArrayList<>();
        if (loop.body instanceof JCBlock) {
            children.add(buildNode((JCBlock) loop.body));
        }
        return children;
    }

    private static List<Node> buildChildren(JCForLoop loop) {
        List<Node> children = new ArrayList<>();
        if (loop.body instanceof JCBlock) {
            children.add(buildNode((JCBlock) loop.body));
        }
        return children;
    }

    private static List<Node> buildChildren(JCEnhancedForLoop loop) {
        List<Node> children = new ArrayList<>();
        if (loop.body instanceof JCBlock) {
            children.add(buildNode((JCBlock) loop.body));
        }
        return children;
    }

    private static List<Node> buildChildren(JCTry jcTry) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(jcTry.body));
        jcTry.catchers.forEach(catcher -> children.add(buildNode(catcher)));

        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            if (def instanceof JCCatch) {
                return buildNode((JCCatch) def);
            }
            if (def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            return null;
        }, jcTry.catchers, jcTry.resources, Collections.singletonList(jcTry.body), Collections.singletonList(jcTry.finalizer));
    }

    private static List<Node> buildChildren(JCReturn jcReturn) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            return null;
        }, Collections.singletonList(jcReturn.expr));
    }

    private static List<Node> buildChildren(JCMethodInvocation jcMethodInvocation) {
        return mapChildren(def -> {
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral){
                return buildNode((JCLiteral)def);
            }
            return null;
        }, Collections.singletonList(jcMethodInvocation.meth), jcMethodInvocation.args);
    }

    private static List<Node> buildChildren(JCBinary jcBinary) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            return null;
        }, Arrays.asList(jcBinary.lhs, jcBinary.rhs));
    }

    private static List<Node> buildChildren(JCIf jcIf) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            return null;
        }, Arrays.asList(jcIf.cond, jcIf.thenpart, jcIf.elsepart));
    }

    private static List<Node> buildChildren(JCCatch jcCatch) {
        List<Node> children = new ArrayList<>();
        children.add(buildNode(jcCatch.body));
        children.add(buildNode(jcCatch.param));
        return children;
    }

    private static List<Node> buildChildren(JCThrow jcThrow) {
        List<Node> children = new ArrayList<>();
        if (jcThrow.expr instanceof JCNewClass) {
            children.add(buildNode((JCNewClass) jcThrow.expr));
        }
        return children;
    }

    private static List<Node> buildChildren(JCNewClass jcNewClass) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            return null;
        }, Collections.singletonList(jcNewClass.clazz), jcNewClass.args);
    }

    private static List<Node> buildChildren(JCParens jcParens) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            return null;
        }, Collections.singletonList(jcParens.expr));
    }

    private static List<Node> mapChildren(Function<JCTree, Node> mapping, Iterable<? extends JCTree>... childCollections) {
        return Arrays.stream(childCollections).flatMap(c -> StreamSupport.stream(c.spliterator(), false))
                .map(mapping)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
