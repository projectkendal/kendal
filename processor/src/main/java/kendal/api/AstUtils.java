package kendal.api;

import java.util.List;

import com.sun.tools.javac.util.Name;

public interface AstUtils {
    Name nameFromString(String name);

    <T> com.sun.tools.javac.util.List<T> toJCList(List<T> name);
}
