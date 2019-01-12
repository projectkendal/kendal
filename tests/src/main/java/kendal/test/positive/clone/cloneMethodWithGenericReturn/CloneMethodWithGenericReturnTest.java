package kendal.test.positive.clone.cloneMethodWithGenericReturn;

import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check whether clone for method with generic return type is created properly
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneMethodWithGenericReturnTest.java
 */
@SuppressWarnings("unused")
public class CloneMethodWithGenericReturnTest {

    @Clone(transformer = TestTransformer.class)
    <T> T method(T param1, String param2) {
        return param1;
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(new SomeType(), s());
    }

    private class SomeType { }

}
