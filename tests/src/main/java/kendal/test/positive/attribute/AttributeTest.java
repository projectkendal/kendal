package kendal.test.positive.attribute;

import static org.testng.Assert.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.testng.annotations.Test;

import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;

/*
 * @test
 * @summary check whether @Attribute works
 * @build BaseAnn
 * @build AttributeTest
 * @run testng kendal.test.positive.attribute.AttributeTest
 */
@SuppressWarnings("unused")
public class AttributeTest {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn(requiredParam = 17))
    @Attribute(name = "requiredAttr", value = "some value")
    @interface UsingAttr {

        String requiredAttr() default "";
    }


    @UsingAttr
    @Test
    public void testMethod() throws NoSuchMethodException {
        assertEquals(getClass().getMethod("testMethod").getAnnotation(UsingAttr.class).requiredAttr(), "some value");
    }

}
