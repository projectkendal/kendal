package kendal.test.positive.clone.IndirectAnnotationDepthTwo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@IndirectAnnotationDepthOne
public @interface IndirectAnnotationDepthTwo {
}
