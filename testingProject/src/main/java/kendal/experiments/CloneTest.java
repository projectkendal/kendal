package kendal.experiments;

import javax.annotation.Generated;

import kendal.annotations.Clone;

public class CloneTest {
    public static final String XYZ = "transformerMethodLoL";

    @Clone(wrapper = TestClassTransformer.class, methodName="transformerMethod", onMethod={@Generated("whatever")})
    public static String aMethod(String param1, int param2) {
        System.out.println("asd");
        try {
            if (12 == 12) return TestClassTransformer.class.newInstance().transform("asd");
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
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

    class TestClassTransformer implements Clone.Transformer<Object, String> {
        @Override
        public String transform(Object input) {
            return "asdas";
        }
    }
}
