package kendal.experiments;

import javax.annotation.Generated;

import kendal.annotations.Clone;

public class CloneTest {

    @Clone(wrapper = TestClassTransformer.class, methodName="transformerMethodABC", onMethod={@Generated("whatever")})
    public static String aMethod(String param1, int param2) {
        return param1 + param2;
    }

    @Generated("whatever")
    public Object aMethodCloneXXX() {
        Object result = aMethod("sdas", 12);
        try {
            return TestClassTransformer.class.newInstance().transform(result);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    class TestClassTransformer implements Clone.Transformer<Object, Object> {
        @Override
        public Object transform(Object input) {
            return input;
        }
    }
}
