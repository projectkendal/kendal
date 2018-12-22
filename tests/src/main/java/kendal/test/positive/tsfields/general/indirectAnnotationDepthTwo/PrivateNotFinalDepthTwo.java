package kendal.test.positive.tsfields.general.indirectAnnotationDepthTwo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@PrivateNotFinalDepthOne
@Target(ElementType.PARAMETER)
public @interface PrivateNotFinalDepthTwo {
}
