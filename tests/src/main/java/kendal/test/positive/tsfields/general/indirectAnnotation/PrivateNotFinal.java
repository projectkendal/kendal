package kendal.test.positive.tsfields.general.indirectAnnotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import kendal.annotations.Private;
import kendal.api.inheritance.Inherit;

@Inherit(@Private(makeFinal = false))
@Target(ElementType.PARAMETER)
@interface PrivateNotFinal {
}
