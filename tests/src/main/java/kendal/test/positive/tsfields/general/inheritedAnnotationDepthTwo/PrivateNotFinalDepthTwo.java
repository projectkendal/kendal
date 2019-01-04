package kendal.test.positive.tsfields.general.inheritedAnnotationDepthTwo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.api.inheritance.Inherit;

@Inherit(@PrivateNotFinalDepthOne)
@Target(ElementType.PARAMETER)
public @interface PrivateNotFinalDepthTwo {
}
