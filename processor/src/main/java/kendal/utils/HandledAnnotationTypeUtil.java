package kendal.utils;

import kendal.api.KendalHandler;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HandledAnnotationTypeUtil {

    private static Map<Class<? extends KendalHandler>, Class<? extends Annotation>> cache = new HashMap();

    public static Class<? extends Annotation> getAnnotationType(Class<? extends KendalHandler> handlerClass) {
        if(cache.containsKey(handlerClass)) {
            return cache.get(handlerClass);
        }

        if(KendalHandler.class.equals(handlerClass) || Object.class.equals(handlerClass)) {
            return null;
        }
        Class<? extends Annotation> annotationType = extractFromInterface(handlerClass.getGenericSuperclass());
        if (annotationType != null) {
            return addToCache(handlerClass, annotationType);
        }

        for (Type iface : handlerClass.getGenericInterfaces()) {
            annotationType = extractFromInterface(iface);
            if (annotationType != null) {
                return addToCache(handlerClass, annotationType);
            }
        }

        annotationType = getAnnotationType((Class<? extends KendalHandler>) handlerClass.getSuperclass());
        if (annotationType != null) {
            return addToCache(handlerClass, annotationType);
        }
        for (Class<?> iface : handlerClass.getInterfaces()) {
            annotationType = getAnnotationType((Class<? extends KendalHandler>) iface);
            if (annotationType != null) {
                return addToCache(handlerClass, annotationType);
            }
        }

        return null;
    }

    private static Class<? extends Annotation> addToCache(Class<? extends KendalHandler> handlerClass, Class<? extends Annotation> value) {
        cache.put(handlerClass, value);
        return value;
    }

    private static Class<? extends Annotation> extractFromInterface(Type iface) {
        if (iface instanceof ParameterizedType) {
            ParameterizedType p = (ParameterizedType)iface;
            Type typeArgument = p.getActualTypeArguments()[0];
            if (typeArgument instanceof Class) {
                if (Annotation.class.isAssignableFrom((Class) typeArgument)) {
                    return (Class<? extends Annotation>) typeArgument;
                }
            }
        }
        return null;
    }
}
