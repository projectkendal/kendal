package kendal.test.positive.clone.inheritingAnnotationDepthTwo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.annotations.Clone;
import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;

@Target(ElementType.ANNOTATION_TYPE)
@Inherit(@Clone(transformer = TestTransformer.class, methodName = "generatedMethod"))
@Attribute(name = "onMethod", value = {@AuxiliaryAnnotation(someFlag = false, value = "value", classArray = {Class.class, String.class})})
@interface IndirectAnnotationDepthOne {
}
