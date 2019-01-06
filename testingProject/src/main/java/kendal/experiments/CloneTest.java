package kendal.experiments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import kendal.annotations.Clone;
import kendal.experiments.subpackage.TestClassTransformer;

public class CloneTest {
    public static final String CLONE_METHOD_NAME = "transformedMethod";

    @Clone(transformer = TestClassTransformer.class, methodName=CLONE_METHOD_NAME, onMethod={@TestAnnotation(value = "nenenene")})
    public <T> String aMethod(T param1, int param2) throws Exception {
        if (param1.equals("abc")) throw new Exception();
        return param1.toString() + param2;
    }

    @WunderClone(endpoint = "theChosenValue")
    public String someMethod5(String param1, int param2) {
        return param1 + param2;
    }

    public void checker() throws Exception {
        transformedMethod("asdas", 21);
        someMethod5Clone("asdas", 21);
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        String value();
        boolean someFlag() default true;
    }

    public @interface RequestMapping {
        String value();
        String method();
    }

    public class CsvTransformer implements Clone.Transformer<Collection<?>, String> {

        @Override
        public String transform(Collection<?> input) {
            return "imagine here is input serialized to csv";
        }
    }
}
