package kendal.test.positive.attrReference;

import static org.testng.Assert.assertEquals;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import org.testng.annotations.Test;

import kendal.annotations.Clone;
import kendal.api.inheritance.AttrReference;
import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;

/*
 * @test
 * @summary check whether @AttrReference works
 * @build BaseAnn
 * @build AttrReferenceTest
 * @run testng kendal.test.positive.attrReference.AttrReferenceTest
 */
@SuppressWarnings("unused")
public class AttrReferenceTest {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@Clone(transformer = Transformer.class))
    @Attribute(name = "onMethod", value = {@InnerAnnotation(value = @AttrReference("attrToRewrite"))})
    @interface UsingAttr {
        String attrToRewrite();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface InnerAnnotation {
        String value();
    }

    class Transformer implements Clone.Transformer<String, List<String>> {
        @Override
        public List<String> transform(String input) {
            return Collections.singletonList(input);
        }
    }

    @UsingAttr(attrToRewrite = "Essen", methodName = "clonedMethod")
    private String method() {
        return "something";
    }

    @Test
    public void testMethod() throws NoSuchMethodException {
        assertEquals(getClass().getDeclaredMethod("clonedMethod").getAnnotation(InnerAnnotation.class).value(), "Essen");

    }

}
