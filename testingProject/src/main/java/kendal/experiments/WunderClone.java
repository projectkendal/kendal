package kendal.experiments;

import kendal.annotations.Clone;
import kendal.api.inheritance.AttrReference;
import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;
import kendal.experiments.subpackage.TestClassTransformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherit(@Clone(transformer = TestClassTransformer.class))
@Attribute.List({
        @Attribute(name = "onMethod", value = {@CloneTest.RequestMapping(value = @AttrReference("endpoint"), method = "POST")})
})
public @interface WunderClone {

    String endpoint() default "someValue";
}
