package kendal.test.positive.inherit;

import static org.testng.Assert.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testng.annotations.Test;

import kendal.api.inheritance.Inherit;

/*
 * @test
 * @summary check if inheriting annotation inherits attributes when they are put on inheriting annotation
 * @build BaseAnn
 * @run testng kendal.test.positive.inherit.InheritWhenParamsDefinedDuringUsageOfInheritingAnnotationTest
 */
@SuppressWarnings("unused")
public class InheritWhenParamsDefinedDuringUsageOfInheritingAnnotationTest {

    private static final int NUMBER = 1;

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn)
    @interface InheritingAnn {

    }

    @InheritingAnn(requiredParam = NUMBER)
    @Test
    public void testMethod() throws NoSuchMethodException {
        assertEquals(getClass().getMethod("testMethod").getAnnotation(InheritingAnn.class).requiredParam(), NUMBER);
    }


}
