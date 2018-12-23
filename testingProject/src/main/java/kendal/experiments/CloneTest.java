package kendal.experiments;

import java.lang.annotation.*;

import kendal.annotations.Clone;
import kendal.api.inheritance.AttrReference;
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
            @Attribute(name = "onMethod", value = {@RequestMapping(value = @AttrReference("endpoint"), method = "POST")})
    })
    @interface WunderClone {

        String endpoint();
    }

    @interface RequestMapping {
        String value();
        String method();
    }

    @interface TestShit {
        String value();
        RequestMapping[] mappings();
    }
}
