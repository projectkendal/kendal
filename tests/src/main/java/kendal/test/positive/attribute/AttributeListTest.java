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
 * @summary check whether \@Attribute works
 * @build BaseAnn
 * @build AttributeTest
 * @run testng kendal.test.positive.attribute.AttributeListTest
 */
@SuppressWarnings("unused")
public class AttributeListTest {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn(requiredParam = 17))
    @Attribute.List({
            @Attribute(name = "requiredAttr", value = "some value")
    })
    @interface UsingAttrList {

        String requiredAttr() default "";
    }


    @UsingAttrList
    @Test
    public void testMethod() throws NoSuchMethodException {
        assertEquals(getClass().getMethod("testMethod").getAnnotation(UsingAttrList.class).requiredAttr(), "some value");
    }

}
