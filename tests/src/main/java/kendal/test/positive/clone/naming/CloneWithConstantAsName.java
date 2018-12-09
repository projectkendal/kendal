package kendal.test.positive.clone.naming;


import java.util.List;

import kendal.annotations.Clone;
import static kendal.test.positive.utils.ValuesGenerator.*;
import kendal.test.positive.clone.naming.TestTransformer;

/*
 * @test
 * @library /positive/utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneWithConstantAsName.java
 */

@SuppressWarnings("unused")
class CloneWithConstantAsName {

    private static final String NEW_METHOD_NAME = "clonedMethod";

    @Clone(transformer = TestTransformer.class, methodName = NEW_METHOD_NAME)
    int method(int param1, String param2) {
        return param1 + param2.length();
    }

    // ### Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return clonedMethod(i(), s());
    }

}
