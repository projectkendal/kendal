package kendal.test.positive.inherit;

import kendal.annotations.Private;
import kendal.api.inheritance.Inherit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/*
 * @test
 * @build BaseAnn
 * @compile InheritHandlerTest.java
 */
@SuppressWarnings("unused")
public class InheritHandlerTest {

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@Private)
    @interface InheritingPrivateHandler{

    }

    class ClassWithFieldGeneratedByInheritedHandler {

        ClassWithFieldGeneratedByInheritedHandler(@InheritingPrivateHandler String field) {

        }

        String methodUsingField() {
            return field;
        }
    }

}
