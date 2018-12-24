package kendal.api.inheritance;

import java.lang.annotation.*;

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


    /**
     * Metadata attribute present on {@link Inherit} annotation usages after compiling inheriting annotation.
     * Its usage in standard way should be avoided and will be discarded.
     *
     * Default is present here only to suppress errors highlighted by IDE, caused by lack of value passed.
     * It will be overriden anyway by Kendal.
     * @return
     */
    Class inheritedAnnotationClass() default Annotation.class;
}
