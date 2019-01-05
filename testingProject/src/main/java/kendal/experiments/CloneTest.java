package kendal.experiments;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import kendal.annotations.Clone;
import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;
import kendal.experiments.subpackage.TestClassTransformer;

public class CloneTest {
    public static final String CLONE_METHOD_NAME = "transformedMethod";

    @Clone(transformer = TestClassTransformer.class, methodName=CLONE_METHOD_NAME, onMethod={@TestAnnotation(value = "nenenene")})
    public <T> String aMethod(T param1, int param2) throws Exception {
        if (param1.equals("abc")) throw new Exception();
        return param1.toString() + param2;
    }

    @IndirectClone
    public <T> String someMethod(T param1, int param2) throws Exception {
        if (param1.equals("abc")) throw new Exception();
        return param1.toString() + param2;
    }

    @IndirectClone
    public <T> String someMethod2(T param1, int param2) {
        return param1.toString() + param2;
    }

    @EvenMoreIndirectClone
    public <T> String someMethod4(T param1, int param2) {
        return param1.toString() + param2;
    }

    @NamedIndirectClone
    public String someMethod3(String param1, int param2) {
        return param1 + param2;
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TestAnnotation {
        String value();
        boolean someFlag() default true;
    }

    @Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @Clone(transformer = TestClassTransformer.class, onMethod={@TestAnnotation(value = "OPS")})
    @interface IndirectClone {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @IndirectClone
    @interface EvenMoreIndirectClone {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Clone(transformer = TestClassTransformer.class, methodName = "indirectMethodName")
    @interface NamedIndirectClone {
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Inherit(@Clone)
    @Attribute.List({
//            @Attribute(name = "onMethod", value = {@RequestMapping(value = @AttrReference("endpoint"), method = "POST")})
    })
    @interface WunderClone {

        String endpoint() default "someValue";
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
