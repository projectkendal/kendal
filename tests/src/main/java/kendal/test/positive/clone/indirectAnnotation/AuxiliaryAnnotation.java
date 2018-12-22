package kendal.test.positive.clone.indirectAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuxiliaryAnnotation {

    String value();
    boolean someFlag() default true;
    Class[] classArray() default {};

}
