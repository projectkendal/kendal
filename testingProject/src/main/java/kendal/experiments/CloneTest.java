package kendal.experiments;

import javax.annotation.Generated;

import kendal.annotations.Clone;
import kendal.experiments.subpackage.TestClassTransformer;

public class CloneTest {
    public static final String CLONE_METHOD_NAME = "transformedMethod";

    @Clone(transformer = TestClassTransformer.class, methodName=CLONE_METHOD_NAME, onMethod={@Generated("whatever")})
    public String aMethod(String param1, int param2) {
        return param1 + param2;
    }

}
