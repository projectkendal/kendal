package kendal.api.impl;

import com.sun.tools.javac.util.Context;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import kendal.api.AstUtils;

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

}
