package kendal.utils;

import java.util.function.Consumer;
import java.util.function.Function;

public class Utils {

    public static <T> void ifNotNull(T object, Consumer<T> consumer) {
        if(object != null) {
            consumer.accept(object);
        }
    }

    public static <T, R> R ifNotNull(T object, Function<T, R> function, R otherwise) {
        if (object != null) {
            return function.apply(object);
        }
        return otherwise;
    }

    public static <T> void with(T object, Consumer<T> consumer) {
        consumer.accept(object);
    }
}
