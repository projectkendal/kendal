package kendal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(value= ElementType.PARAMETER)
public @interface PackagePrivate {

    boolean makeFinal() default true;
}