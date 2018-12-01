package kendal.experiments;

import javax.annotation.Generated;

import kendal.annotations.Clone;

public class CloneTest {
    public static final String XYZ = "clonner";

    @Clone(wrapper = TestClassTransformer.class, methodName=XYZ, onMethod={@Generated("whatever")})
    public String aMethod(String param1, int param2) throws InstantiationException, IllegalAccessException {
        System.out.println("asd");
        if ("as" == "") throw new InstantiationException("ds");
        if (12 == 13) throw new IllegalAccessException("ds");
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

    class TestClassTransformer implements Clone.Transformer<Object, String> {
        @Override
        public String transform(Object input) {
            return "asdas";
        }
    }
}
