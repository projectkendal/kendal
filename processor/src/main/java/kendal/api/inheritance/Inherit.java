package kendal.api.inheritance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 1. This annotation causes all parameters from #value() to be inherited.
 *    #value() must be an annotation.
 *
 * 2. Kendal handlers handling #value() will receive also annotations that inherit parameters from #value() using {@link Inherit}.
 * Note that this relation is transitive.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Inherit {
    //commented out, but we process it
//        Annotation value();
}
