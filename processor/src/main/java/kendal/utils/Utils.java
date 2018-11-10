package kendal.utils;

import java.util.function.Consumer;

public class Utils {

    public static <T> void ifNotNull(T object, Consumer<T> consumer) {
        if(object != null) {
            consumer.accept(object);
        }
    }

    public static <T> void with(T object, Consumer<T> consumer) {
        consumer.accept(object);
    }
}
