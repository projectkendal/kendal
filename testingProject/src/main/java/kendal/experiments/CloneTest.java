package kendal.experiments;

import javax.annotation.Generated;

import kendal.annotations.Clone;

public class CloneTest {
    public static final String XYZ = "transformerMethodLoL";

    @Clone(wrapper = TestClassTransformer.class, methodName="transformerMethod", onMethod={@Generated("whatever")})
    public static String aMethod(String param1, int param2) {
        return param1 + param2;
    }

    @Generated("whatever")
    public Object something() {
        Object result = aMethod("sdas", 12);
        try {
            return TestClassTransformer.class.newInstance().transform(result);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static String transformerMethod(String param1, int param2) {
        return "abc";
    }

    public static String transformerMethod1(String param1, int param2) {
        return "abc";
    }

    public static String transformerMethod2(String param1, int param2) {
        return "abc";
    }

    public static String transformerMethod3(int param1, int param2) {
        return "abc";
    }

    class TestClassTransformer implements Clone.Transformer<Object, Object> {
        @Override
        public Object transform(Object input) {
            return input;
        }
    }
}
