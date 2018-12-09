package kendal.test.positive.clone.naming;

import static kendal.test.positive.utils.ValuesGenerator.i;
import static kendal.test.positive.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;
/*
 * @test
 * @library /positive/utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneWithDefaultName.java
 */
@SuppressWarnings("unused")
class CloneWithDefaultName {

    @Clone(transformer = TestTransformer.class)
    int method(int param1, String param2) {
        return param1 + param2.length();
    }

    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(i(), s());
    }
}
