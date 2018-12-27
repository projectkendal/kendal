package kendal.test.positive.inherit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface BaseAnn {

    String defaultParam() default "some value";
    int requiredParam();
}
