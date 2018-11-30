package kendal.experiments;

import javax.annotation.Generated;

import kendal.annotations.Clone;

public class CloneTest {
    public static final String XYZ = "transformerMethodLoL";

    @Clone(wrapper = TestClassTransformer.class, methodName="transformerMethod", onMethod={@Generated("whatever")})
    public String aMethod(String param1, int param2) {
        System.out.println("asd");
        return param1 + param2;
    }

    @Generated("whatever")
    public Object something(String param1, int param2) {
        try {
            return TestClassTransformer.class.newInstance().transform(aMethod("sdas", 12));
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private String lol(String param2) {
        return aMethod(param2, 12);
    }

    class TestClassTransformer implements Clone.Transformer<Object, String> {
        @Override
        public String transform(Object input) {
            return "asdas";
        }
    }
}
