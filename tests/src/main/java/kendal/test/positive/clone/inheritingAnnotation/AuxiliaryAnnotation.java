package kendal.test.positive.clone.inheritingAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@interface AuxiliaryAnnotation {

    String value();
    boolean someFlag() default true;
    Class[] classArray() default {};

}
