package kendal.test.positive.clone.indirectAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.annotations.Clone;
import kendal.api.inheritance.Inherit;
import kendal.api.inheritance.Attribute;

@Target(ElementType.METHOD)
@Inherit(@Clone(transformer = TestTransformer.class, methodName = "generatedMethod"))
@Attribute(name = "onMethod", value = {@AuxiliaryAnnotation(someFlag = false, value = "value", classArray = {Class.class, String.class})})
@interface IndirectAnnotation {
}
