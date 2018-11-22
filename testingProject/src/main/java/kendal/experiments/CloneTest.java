package kendal.experiments;

import kendal.annotations.Clone;

import javax.annotation.Generated;

public class CloneTest {


    @Clone(wrapper = TestClassTransformer.class, methodName="transformerMethod", onMethod={@Generated("whatever")})
    public Object aMethod() {
        return new Object();
    }

    @Generated("whatever")
    public Object aMethodClone() {
        Object result = aMethod();
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
