package kendal.test.negative.inherit;

import kendal.api.inheritance.Inherit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @test
 * @build BaseAnn
 * @compile/fail/ref=Inherit_OverrideRequiredParam_InvalidValue_Test.out Inherit_OverrideRequiredParam_InvalidValue_Test.java -XDrawDiagnostics
 */
@SuppressWarnings("unused")
public class Inherit_OverrideRequiredParam_InvalidValue_Test {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn(requiredParam = "string"))
    @interface InheritingAnnOverridingParam{

    }

    @InheritingAnnOverridingParam
    private void anotherMethod() {

    }

}
