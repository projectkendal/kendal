package kendal.test.positive.inherit;

import kendal.api.inheritance.Inherit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @test
 * @compile InheritParamsTest.java
 */
@SuppressWarnings("unused")
public class InheritParamsTest {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn)
    @interface InheritingAnn {

    }


    @InheritingAnn(requiredParam = 1)
    private void method() {

    }


}
