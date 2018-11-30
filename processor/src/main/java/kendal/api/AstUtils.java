package kendal.api;

import java.util.List;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Name;

import kendal.model.Node;

public interface AstUtils {
    Name nameFromString(String name);

    <T> com.sun.tools.javac.util.List<T> toJCList(List<T> list);

    <T> com.sun.tools.javac.util.List<T> toJCList(T element);

    <T extends JCTree> com.sun.tools.javac.util.List<T> mapNodesToJCList(List<Node<T>> listOfNodes);
}
