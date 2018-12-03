package kendal.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import javax.annotation.Generated;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Clone {

    Class<? extends Transformer> transformer();

    /**
    * Java does not allow putting {@link java.lang.annotation.Annotation}, which is the base type for all annotations, as annotation member.
    * Let's just put here a placeholder and handle any type during Annotation Processing
    */
    //TODO maybe just remove the field and handle it anyway during processing?
    Generated[] onMethod() default {};

    String methodName() default ""; //defaults to original methodName + "^Clone[0-9]*$"

    interface Transformer<T,R> {
        R transform(T input);
    }
}
