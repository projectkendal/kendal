package kendal.test.positive.clone.cloneMethodWithGenericArg;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check whether clone for method with generic argument is created properly
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneMethodWithGenericArg.java
 */
@SuppressWarnings("unused")
public class CloneMethodWithGenericArg {

    @Clone(transformer = TestTransformer.class)
    <T> int method(T param1, String param2) {
        return param1.toString().length() + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(i(), s());
    }

}
