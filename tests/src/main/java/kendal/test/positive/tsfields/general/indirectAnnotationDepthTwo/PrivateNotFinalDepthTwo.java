package kendal.test.positive.tsfields.general.indirectAnnotationDepthTwo;

import kendal.api.inheritance.Inherit;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Inherit(@PrivateNotFinalDepthOne)
@Target(ElementType.PARAMETER)
public @interface PrivateNotFinalDepthTwo {
}
