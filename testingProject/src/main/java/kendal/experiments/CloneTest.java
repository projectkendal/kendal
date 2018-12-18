package kendal.experiments;

import javax.annotation.Generated;

import kendal.annotations.Clone;
import kendal.experiments.subpackage.TestClassTransformer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CloneTest {
    public static final String CLONE_METHOD_NAME = "transformedMethod";

    @Clone(transformer = TestClassTransformer.class, methodName=CLONE_METHOD_NAME, onMethod={@TestAnnotation(value = "nenenene")})
    public <T> String aMethod(T param1, int param2) throws Exception {
        if (param1.equals("abc")) throw new Exception();
        return param1.toString() + param2;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        String value();
        boolean someFlag() default true;
    }

}
