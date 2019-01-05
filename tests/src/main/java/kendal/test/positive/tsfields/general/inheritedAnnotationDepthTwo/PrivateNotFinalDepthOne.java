package kendal.test.positive.tsfields.general.inheritedAnnotationDepthTwo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.annotations.Private;
import kendal.api.inheritance.Inherit;

@Inherit(@Private(makeFinal = false))
@Target(ElementType.ANNOTATION_TYPE)
@interface PrivateNotFinalDepthOne {
}
