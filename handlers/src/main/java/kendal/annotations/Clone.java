package kendal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Applied to a method creates new method with name from {@link #methodName()} attribute and list of parameters same as original method.
 * Generated method calls the original method, passes the result to an instance of {@link #transformer()}
 * and returns transformed value. If {@link #methodName()} is not specified, clone with default name is created.
 * Default name is: originalName + "Clone"
 * Return type of generated method is extracted from declaration of {@link #transformer()} class.
 */
@Target(ElementType.METHOD)
public @interface Clone {

    Class<? extends Transformer> transformer();

    /**
     * Java does not allow putting {@link java.lang.annotation.Annotation}, which is the base type for all annotations, as annotation member.
     * Let's just use this field as if it was defined.
     * We remove onMethod parameter from AST after processing @Clone usage.
     * /
//    Annotation[] onMethod() default {};

    /**
     * Name of the generated method.
     * Defaults to original method name + "Clone" suffix
     * */
    String methodName() default "";

    interface Transformer<T,R> {
        R transform(T input);
    }
}
