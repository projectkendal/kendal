package kendal.test.positive.clone.naming;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check whether created clone method has proper name when it is defined using constant
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneWithConstantAsNameTest.java
 */

@SuppressWarnings("unused")
class CloneWithConstantAsNameTest {

    private static final String NEW_METHOD_NAME = "clonedMethod";

    @Clone(transformer = TestTransformer.class, methodName = NEW_METHOD_NAME)
    int method(int param1, String param2) {
        return param1 + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return clonedMethod(i(), s());
    }

}
