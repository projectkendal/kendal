package kendal.test.positive.clone.indirectAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.annotations.Clone;

@Target(ElementType.METHOD)
@Clone(transformer = TestTransformer.class)
public @interface IndirectAnnotation {
}
