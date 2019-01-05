package kendal.test.negative.attribute;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import kendal.annotations.Clone;
import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;

/*
 * @test
 * @summary check whether \@Attribute does not work when trying to define nonexistent attribute
 * @build BaseAnn
 * @compile/fail/ref=Attribute_InvalidAttrTest.out Attribute_InvalidAttrTest.java -XDrawDiagnostics
 */
@SuppressWarnings("unused")
public class Attribute_InvalidAttrTest {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@Clone(transformer = Transformer.class))
    @Attribute(name = "notExistingAttribute", value = "does not matter")
    @interface UsingAttr {
    }

    class Transformer implements Clone.Transformer<String, List<String>> {
        @Override
        public List<String> transform(String input) {
            return Collections.singletonList(input);
        }
    }

    @UsingAttr
    private String method() {
        return "something";
    }

}
