package kendal.test.negative.attrReference;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.List;

import kendal.annotations.Clone;
import kendal.api.inheritance.AttrReference;
import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;

/*
 * @test
 * @summary check whether \@AttrReference does not work when trying to reference nonexistent attribute
 * @build BaseAnn
 * @compile/fail/ref=AttrReference_InvalidAttrTest.out AttrReference_InvalidAttrTest.java -XDrawDiagnostics
 */
@SuppressWarnings("unused")
public class AttrReference_InvalidAttrTest {

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@Clone(transformer = Transformer.class))
    @Attribute(name = "onMethod", value = {@InnerAnnotation(value = @AttrReference("notExistingAttr"))})
    @interface UsingAttr {
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

    @UsingAttr
    private String method() {
        return "something";
    }

}
