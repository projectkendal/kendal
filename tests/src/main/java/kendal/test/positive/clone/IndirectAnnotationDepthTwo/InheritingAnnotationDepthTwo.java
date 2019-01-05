package kendal.test.positive.clone.IndirectAnnotationDepthTwo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.api.inheritance.Inherit;

@Target(ElementType.METHOD)
@Inherit(@IndirectAnnotationDepthOne)
@interface InheritingAnnotationDepthTwo {
}
