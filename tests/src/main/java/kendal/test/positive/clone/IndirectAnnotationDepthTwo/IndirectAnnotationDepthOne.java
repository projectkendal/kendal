package kendal.test.positive.clone.IndirectAnnotationDepthTwo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.annotations.Clone;

@Target(ElementType.ANNOTATION_TYPE)
@Clone(transformer = TestTransformer.class, methodName = "generatedMethod",
        onMethod = {@AuxiliaryAnnotation(someFlag = false, value = "value", classArray = {Class.class, String.class})})
public @interface IndirectAnnotationDepthOne {
}
