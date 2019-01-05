package kendal.test.positive.clone.naming;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;
/*
 * @test
 * @summary check whether created clone method has default name when no other name is specified
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneWithDefaultNameTest.java
 */
@SuppressWarnings("unused")
class CloneWithDefaultNameTest {

    @Clone(transformer = TestTransformer.class)
    int method(int param1, String param2) {
        return param1 + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(i(), s());
    }
}
