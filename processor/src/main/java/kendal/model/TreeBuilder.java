package kendal.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.*;

@SuppressWarnings("Duplicates")
public class TreeBuilder {

    private TreeBuilder() {
        // private constructor to hide default public one
    }

    static Node buildTree(JCCompilationUnit compilationUnit) {
        return buildNode(compilationUnit);
    }

    public static Node<JCCompilationUnit> buildNode(JCCompilationUnit jcCompilationUnit) {
        return new Node<>(jcCompilationUnit, buildChildren(jcCompilationUnit));
    }

    public static Node<JCClassDecl> buildNode(JCClassDecl jcClassDecl) {
        return new Node<>(jcClassDecl, buildChildren(jcClassDecl));
    }

    public static Node<JCMethodDecl> buildNode(JCMethodDecl jcMethodDecl) {
        return new Node<>(jcMethodDecl, buildChildren(jcMethodDecl));
    }

    public static Node<JCBlock> buildNode(JCBlock jcBlock) {
        return new Node<>(jcBlock, buildChildren(jcBlock));
    }

    public static Node<JCVariableDecl> buildNode(JCVariableDecl jcVariableDecl) {
        return new Node<>(jcVariableDecl, buildChildren(jcVariableDecl));
    }

    public static Node<JCWhileLoop> buildNode(JCWhileLoop jcWhileLoop) {
        return new Node<>(jcWhileLoop, buildChildren(jcWhileLoop));
    }

    public static Node<JCDoWhileLoop> buildNode(JCDoWhileLoop jcDoWhileLoop) {
        return new Node<>(jcDoWhileLoop, buildChildren(jcDoWhileLoop));
    }

    public static Node<JCForLoop> buildNode(JCForLoop jcForLoop) {
        return new Node<>(jcForLoop, buildChildren(jcForLoop));
    }

    public static Node<JCEnhancedForLoop> buildNode(JCEnhancedForLoop jcEnhancedForLoop) {
        return new Node<>(jcEnhancedForLoop, buildChildren(jcEnhancedForLoop));
    }

    public static Node<JCImport> buildNode(JCImport jcImport) {
        return new Node<>(jcImport);
    }

    public static Node<JCAnnotation> buildNode(JCAnnotation jcAnnotation) {
        return new Node<>(jcAnnotation, buildChildren(jcAnnotation));
    }

    public static Node<JCAssign> buildNode(JCAssign jcAssign) {
        return new Node<>(jcAssign, buildChildren(jcAssign));
    }

    public static Node<JCNewArray> buildNode(JCNewArray jcNewArray) {
        return new Node<>(jcNewArray, buildChildren(jcNewArray));
    }

    public static Node<JCExpressionStatement> buildNode(JCExpressionStatement jcExpressionStatement) {
        return new Node<>(jcExpressionStatement, buildChildren(jcExpressionStatement));
    }

    public static Node<JCMethodInvocation> buildNode(JCMethodInvocation jcMethodInvocation) {
        return new Node<>(jcMethodInvocation, buildChildren(jcMethodInvocation));
    }

    public static Node<JCBinary> buildNode(JCBinary jcBinary) {
        return new Node<>(jcBinary, buildChildren(jcBinary));
    }

    public static Node<JCUnary> buildNode(JCUnary jcUnary) {
        return new Node<>(jcUnary, buildChildren(jcUnary));
    }

    public static Node<JCIdent> buildNode(JCIdent jcIdent) {
        return new Node<>(jcIdent);
    }

    public static Node<JCLiteral> buildNode(JCLiteral jcLiteral) {
        return new Node<>(jcLiteral);
    }

    public static Node<JCTry> buildNode(JCTry jcTry) {
        return new Node<>(jcTry, buildChildren(jcTry));
    }

    public static Node<JCReturn> buildNode(JCReturn jcReturn) {
        return new Node<>(jcReturn, buildChildren(jcReturn));
    }

    public static Node<JCIf> buildNode(JCIf jcIf) {
        return new Node<>(jcIf, buildChildren(jcIf));
    }

    public static Node<JCCatch> buildNode(JCCatch jcCatch) {
        return new Node<>(jcCatch, buildChildren(jcCatch));
    }

    public static Node<JCThrow> buildNode(JCThrow jcThrow) {
        return new Node<>(jcThrow, buildChildren(jcThrow));
    }

    public static Node<JCNewClass> buildNode(JCNewClass jcNewClass) {
        return new Node<>(jcNewClass, buildChildren(jcNewClass));
    }

    public static Node<JCParens> buildNode(JCParens jcParens) {
        return new Node<>(jcParens, buildChildren(jcParens));
    }

    public static Node<JCTypeUnion> buildNode(JCTypeUnion jcTypeUnion) {
        return new Node<>(jcTypeUnion, buildChildren(jcTypeUnion));
    }

    public static Node<JCFieldAccess> buildNode(JCFieldAccess jcFieldAccess) {
        return new Node<>(jcFieldAccess, buildChildren(jcFieldAccess));
    }

    public static Node<JCTypeParameter> buildNode(JCTypeParameter jcFieldAccess) {
        return new Node<>(jcFieldAccess);
    }

    private static List<Node> buildChildren(JCCompilationUnit compilationUnit) {
        return mapChildren(def -> {
            if (def instanceof JCClassDecl) {
                return buildNode((JCClassDecl) def);
            }
            if (def instanceof JCImport) {
                return buildNode((JCImport) def);
            }
            return null;
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
            if (def instanceof JCAnnotation) {
                return buildNode((JCAnnotation) def);
            }
            return null;
        }, classDecl.defs, classDecl.mods.annotations);

    }

    private static List<Node> buildChildren(JCMethodDecl methodDecl) {
        return mapChildren(def -> {
            if (def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            if (def instanceof JCAnnotation) {
                return buildNode((JCAnnotation) def);
            }
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCTypeParameter) {
                return buildNode((JCTypeParameter) def);
            }
            return null;
        }, methodDecl.params, methodDecl.mods.annotations, Collections.singletonList(methodDecl.body), methodDecl.thrown,
                methodDecl.typarams);
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
            if (def instanceof JCNewClass) {
                return buildNode((JCNewClass) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCUnary) {
                return buildNode((JCUnary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCAnnotation) {
                return buildNode((JCAnnotation) def);
            }
            return null;
        }, Collections.singletonList(variableDecl.init), variableDecl.mods.annotations);
    }

    private static List<Node> buildChildren(JCAnnotation jcAnnotation) {
        return mapChildren(def -> {
            if(def instanceof JCAssign) {
                return buildNode((JCAssign)def);
            }
            if(def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            return null;
        }, jcAnnotation.args);
    }

    private static List<Node> buildChildren(JCAssign jcAssign) {
        return mapChildren(def -> {
            if(def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if(def instanceof JCFieldAccess) {
                return buildNode((JCFieldAccess) def);
            }
            if(def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            if(def instanceof JCAnnotation) {
                return buildNode((JCAnnotation) def);
            }
            if(def instanceof JCNewArray) {
                return buildNode((JCNewArray) def);
            }
            return null;
        }, Arrays.asList(jcAssign.lhs, jcAssign.rhs));
    }

    private static List<Node> buildChildren(JCNewArray jcNewArray) {
        return mapChildren(def -> {
            if(def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if(def instanceof JCFieldAccess) {
                return buildNode((JCFieldAccess) def);
            }
            if(def instanceof JCLiteral) {
                return buildNode((JCLiteral) def);
            }
            if(def instanceof JCAnnotation) {
                return buildNode((JCAnnotation) def);
            }
            if(def instanceof JCNewArray) {
                return buildNode((JCNewArray) def);
            }
            return null;
        }, jcNewArray.elems);
    }

    private static List<Node> buildChildren(JCWhileLoop loop) {
        return mapChildren(def -> {
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            return null;
        }, Arrays.asList(loop.body, loop.cond));
    }

    private static List<Node> buildChildren(JCDoWhileLoop loop) {
        return mapChildren(def -> {
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            return null;
        }, Arrays.asList(loop.body, loop.cond));
    }

    private static List<Node> buildChildren(JCForLoop loop) {
        return mapChildren(def -> {
            if (def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            if (def instanceof JCExpressionStatement) {
                return buildNode((JCExpressionStatement) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            return null;
        }, Arrays.asList(loop.body, loop.cond), loop.init, loop.step);
    }

    private static List<Node> buildChildren(JCEnhancedForLoop loop) {
        return mapChildren(def -> {
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            if (def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            return null;
        }, Arrays.asList(loop.body, loop.var, loop.expr));
    }

    private static List<Node> buildChildren(JCExpressionStatement jcExpressionStatement) {
        return mapChildren(def -> {
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral){
                return buildNode((JCLiteral) def);
            }
            if (def instanceof JCFieldAccess){
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCUnary) {
                return buildNode((JCUnary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCNewClass) {
                return buildNode((JCNewClass) def);
            }
            return null;
        }, Collections.singletonList(jcExpressionStatement.expr));
    }

    private static List<Node> buildChildren(JCTry jcTry) {
        return mapChildren(def -> {
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
            if (def instanceof JCUnary) {
                return buildNode((JCUnary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCFieldAccess) {
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCNewClass) {
                return buildNode((JCNewClass) def);
            }
            return null;
        }, Collections.singletonList(jcReturn.expr));
    }

    private static List<Node> buildChildren(JCMethodInvocation jcMethodInvocation) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCLiteral){
                return buildNode((JCLiteral) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCBinary) {
                return buildNode((JCBinary) def);
            }
            if (def instanceof JCUnary) {
                return buildNode((JCUnary) def);
            }
            if (def instanceof JCParens){
                return buildNode((JCParens) def);
            }
            if (def instanceof JCFieldAccess){
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCNewClass) {
                return buildNode((JCNewClass) def);
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
            if (def instanceof JCUnary) {
                return buildNode((JCUnary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCFieldAccess){
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCNewClass){
                return buildNode((JCNewClass) def);
            }
            return null;
        }, Arrays.asList(jcBinary.lhs, jcBinary.rhs));
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
            if (def instanceof JCUnary) {
                return buildNode((JCUnary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCFieldAccess){
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCNewClass){
                return buildNode((JCNewClass) def);
            }
            return null;
        }, Collections.singletonList(jcParens.expr));
    }

    private static List<Node> buildChildren(JCNewClass jcNewClass) {
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
            if (def instanceof JCUnary) {
                return buildNode((JCUnary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCFieldAccess){
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCNewClass){
                return buildNode((JCNewClass) def);
            }
            return null;
        }, Collections.singletonList(jcNewClass.clazz), jcNewClass.args);
    }

    private static List<Node> buildChildren(JCUnary jcUnary) {
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
            if (def instanceof JCUnary) {
                return buildNode((JCUnary) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            if (def instanceof JCFieldAccess){
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCNewClass){
                return buildNode((JCNewClass) def);
            }
            return null;
        }, Arrays.asList(jcUnary.arg));
    }

    private static List<Node> buildChildren(JCIf jcIf) {
        return mapChildren(def -> {
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
        return mapChildren(def -> {
            if (def instanceof JCBlock) {
                return buildNode((JCBlock) def);
            }
            if (def instanceof JCVariableDecl) {
                return buildNode((JCVariableDecl) def);
            }
            return null;
        }, Arrays.asList(jcCatch.body, jcCatch.param));
    }

    private static List<Node> buildChildren(JCThrow jcThrow) {
        return mapChildren(def -> {
            if (def instanceof JCNewClass) {
                return buildNode((JCNewClass) def);
            }
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCFieldAccess) {
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            return null;
        }, Collections.singletonList(jcThrow.expr));
    }

    private static List<Node> buildChildren(JCTypeUnion typeUnion) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            return null;
        }, typeUnion.alternatives);
    }

    private static List<Node> buildChildren(JCFieldAccess fieldAccess) {
        return mapChildren(def -> {
            if (def instanceof JCIdent) {
                return buildNode((JCIdent) def);
            }
            if (def instanceof JCMethodInvocation) {
                return buildNode((JCMethodInvocation) def);
            }
            if (def instanceof JCFieldAccess) {
                return buildNode((JCFieldAccess) def);
            }
            if (def instanceof JCNewClass) {
                return buildNode((JCNewClass) def);
            }
            if (def instanceof JCParens) {
                return buildNode((JCParens) def);
            }
            return null;
        }, Collections.singletonList(fieldAccess.selected));
    }

    private static List<Node> mapChildren(Function<JCTree, Node> mapping, Iterable<? extends JCTree>... childCollections) {
        return Arrays.stream(childCollections).flatMap(c -> StreamSupport.stream(c.spliterator(), false))
                .map(mapping)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    static void finishInitialPhase() {
        Node.finishInitialPhase();
    }

}
