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
 * @summary check if inheriting annotation inherits attributes when they are put in source of inheriting annotation and then overridden
 * @build BaseAnn
 * @run testng kendal.test.positive.inherit.InheritWhenParamsRedefinedDuringUsageOfInheritingAnnotationTest
 */
@SuppressWarnings("unused")
public class InheritWhenParamsRedefinedDuringUsageOfInheritingAnnotationTest {
    private static final int NUMBER1 = 1;
    private static final int NUMBER2 = 2;

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn(requiredParam = NUMBER1))
    @interface InheritingAnnOverridingParam{

    }

    @InheritingAnnOverridingParam(requiredParam = NUMBER2)
    @Test
    public void testMethod() throws NoSuchMethodException {
        assertEquals(getClass().getMethod("testMethod").getAnnotation(InheritingAnnOverridingParam.class).requiredParam(), NUMBER2);
    }
}
