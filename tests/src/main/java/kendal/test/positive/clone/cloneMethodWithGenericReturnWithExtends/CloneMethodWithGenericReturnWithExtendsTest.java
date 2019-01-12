package kendal.test.positive.clone.cloneMethodWithGenericReturnWithExtends;

import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check whether clone for method with generic with extends return type is created properly
 * @library /utils/
 * @build SuperType
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneMethodWithGenericReturnWithExtendsTest.java
 */
@SuppressWarnings("unused")
public class CloneMethodWithGenericReturnWithExtendsTest {

    @Clone(transformer = TestTransformer.class)
    <T extends SuperType> T method(T param1, String param2) {
        return param1;
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(new SomeType(), s());
    }

    private class SomeType extends SuperType { }

}
