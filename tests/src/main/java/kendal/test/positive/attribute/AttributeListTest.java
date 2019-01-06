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
 * @summary check whether \@Attribute works
 * @build BaseAnn
 * @build AttributeTest
 * @run testng kendal.test.positive.attribute.AttributeListTest
 */
@SuppressWarnings("unused")
public class AttributeListTest {

    private static final int NUMBER = 17;
    private static final String TEXT = "some value";

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@BaseAnn)
    @Attribute.List({
            @Attribute(name = "requiredParam", value = NUMBER),
            @Attribute(name = "requiredAttr", value = TEXT)
    })
    @interface UsingAttrList {

        String requiredAttr() default "";
    }


    @UsingAttrList
    @Test
    public void testMethod() throws NoSuchMethodException {
        assertEquals(getClass().getMethod("testMethod").getAnnotation(UsingAttrList.class).requiredParam(), NUMBER);
        assertEquals(getClass().getMethod("testMethod").getAnnotation(UsingAttrList.class).requiredAttr(), TEXT);
    }

}
