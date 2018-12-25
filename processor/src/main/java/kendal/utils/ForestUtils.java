package kendal.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import kendal.model.Node;

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
