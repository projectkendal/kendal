package kendal.test.positive.attribute;

import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;
import org.testng.annotations.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.testng.Assert.assertEquals;

/*
 * @test
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
