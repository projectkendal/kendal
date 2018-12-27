package kendal.test.negative.inherit;

import kendal.api.inheritance.Inherit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @test
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
