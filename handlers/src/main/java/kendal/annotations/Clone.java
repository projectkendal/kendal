package kendal.annotations;

import javax.annotation.Generated;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
public @interface Clone {

    Class wrapper();

    /**
    * Java does not allow putting {@link java.lang.annotation.Annotation}, which is the base type for all annotations, as annotation member.
    * Let's just put here a placeholder and handle any type during Annotation Processing
    */
    //TODO maybe just remove the field and handle it anyway during processing?
    Generated[] onMethod() default {};

    String methodName() default ""; //default name from original method + something, if this is empty

}
