package kendal.test.positive.clone.cloneGenericMethod;

import static kendal.test.positive.utils.ValuesGenerator.i;
import static kendal.test.positive.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @library /positive/utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneGenericMethod.java
 */
@SuppressWarnings("unused")
public class CloneGenericMethod {

    @Clone(transformer = TestTransformer.class)
    <T> int method(T param1, String param2) {
        return param1.toString().length() + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(i(), s());
    }

}
