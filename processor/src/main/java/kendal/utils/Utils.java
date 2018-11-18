package kendal.utils;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

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

    public static <T, R> R map(T obj, Function<T,R> mapping) {
        return Stream.of(obj).map(mapping).findFirst().orElse(null);
    }

    public static <T> T map(T obj, Consumer<T> consumer) {
        consumer.accept(obj);
        return obj;
    }

    public static <T> void with(T object, Consumer<T> consumer) {
        consumer.accept(object);
    }
}
