package kendal.utils;

import java.util.*;
import java.util.function.Consumer;

import com.sun.tools.javac.tree.JCTree;
import kendal.model.Node;

import static kendal.utils.AnnotationUtils.isAnnotationType;

public class ForestUtils {

    private ForestUtils() {
        // prevent instantiation
    }

    public static void traverse(Node root, Consumer<Node> consumer) {
        new Traverser(Collections.singleton(root), consumer).traverse();
    }

    public static void traverse(Collection<Node> roots, Consumer<Node> consumer) {
        new Traverser(roots, consumer).traverse();
    }

    /**
     * Extract from the forest a map containing fully qualified name as key and node with annotation definition as value.
     * @param forest
     * @return
     */
    public static Map<String, Node<JCTree.JCClassDecl>> getAnnotationsDeclMap(Set<Node> forest) {
        Map<String, Node<JCTree.JCClassDecl>> result = new HashMap<>();
        ForestUtils.traverse(forest, node -> {
            if(isAnnotationType(node)) {
                result.put(((JCTree.JCClassDecl) node.getObject()).sym.type.tsym.getQualifiedName().toString(), node);
            }
        });
        return result;
    }


    private static class Traverser {
        private Collection<Node> roots;
        private Consumer<Node> consumer;
        private Set<Node> visited;

        Traverser(Collection<Node> roots, Consumer<Node> consumer) {
            this.roots = roots;
            this.consumer = consumer;
            this.visited = new HashSet<>();

        }

        private void traverse() {
            roots.forEach(this::traverse);
        }

        private void traverse(Node<?> node) {
            if (!visited.contains(node)) {
                visited.add(node);
                consumer.accept(node);
                node.getChildren().forEach(this::traverse);
            }
        }
    }
}
