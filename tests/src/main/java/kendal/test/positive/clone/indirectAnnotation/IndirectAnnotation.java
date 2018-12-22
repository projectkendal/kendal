package kendal.test.positive.clone.indirectAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.annotations.Clone;

@Target(ElementType.METHOD)
@Clone(transformer = TestTransformer.class, methodName = "generatedMethod",
        onMethod = {@AuxiliaryAnnotation(someFlag = false, value = "value", classArray = {Class.class, String.class})})
@interface IndirectAnnotation {
}
