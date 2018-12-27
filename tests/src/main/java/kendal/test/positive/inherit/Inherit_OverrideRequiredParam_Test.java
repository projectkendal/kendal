package kendal.test.positive.inherit;

import kendal.api.inheritance.Inherit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @test
 * @build BaseAnn
 * @compile Inherit_OverrideRequiredParam_Test.java
 */
@SuppressWarnings("unused")
public class Inherit_OverrideRequiredParam_Test {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn(requiredParam = 17))
    @interface InheritingAnnOverridingParam{

    }

    @InheritingAnnOverridingParam
    private void anotherMethod() {

    }

}
