package kendal.api.impl;

import java.util.Collections;
import java.util.stream.Collectors;

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import kendal.api.AstUtils;
import kendal.model.Node;

public class AstUtilsImpl implements AstUtils {

    private final Context context;

    public AstUtilsImpl(Context context) {
        this.context = context;
    }

    @Override
    public Name nameFromString(String name) {
        return Names.instance(context).fromString(name);
    }

    @Override
    public <T> List<T> toJCList(java.util.List<T> list) {
        return com.sun.tools.javac.util.List.from(list);
    }

    @Override
    public <T> List<T> toJCList(T element) {
        return toJCList(Collections.singletonList(element));
    }

    @Override
    public <T extends JCTree> List<T> mapNodesToJCList(java.util.List<Node<T>> listOfNodes) {
        java.util.List<T> list = listOfNodes.stream()
                .map(Node::getObject)
                .collect(Collectors.toList());
        return com.sun.tools.javac.util.List.from(list);
    }

}
