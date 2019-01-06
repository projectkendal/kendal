package kendal.api.inheritance;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation will add named parameter to all usages of annotated annotation.
 * Use {@link List} to add multiple parameters.
 * Parameters will be added to usages of annotation before handlers are called.
 *
 * Use {@link AttrReference} as placeholder for values of any other attributes.
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Attribute {

    String name();

    //commented out, but we process it
//        Object value();

    @Target(ElementType.ANNOTATION_TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        Attribute[] value();
    }
}
