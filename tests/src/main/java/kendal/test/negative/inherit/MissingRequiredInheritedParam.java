package kendal.test.negative.inherit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kendal.api.inheritance.Inherit;

/*
 * @test
 * @summary check whether \@Inherit does not work when required parameter is missing
 * @build BaseAnn
 * @compile/fail/ref=MissingRequiredInheritedParam.out MissingRequiredInheritedParam.java -XDrawDiagnostics
 */
@SuppressWarnings("unused")
public class MissingRequiredInheritedParam {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn)
    @interface InheritingAnn {

    }


    @InheritingAnn()
    private void method() {

    }


}
