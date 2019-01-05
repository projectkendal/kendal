package kendal.test.positive.clone.transformerWithDependencies;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check whether clone method is properly created when transformer has two methods named "transform"
 * @library /utils/
 * @build ValuesGenerator
 * @build AbstractClassWithTransformMethod
 * @build TransformerWithDependencies
 * @compile CloneWithTransformerHavingTwoTransformMethodsTest.java
 */
@SuppressWarnings("unused")
public class CloneWithTransformerHavingTwoTransformMethodsTest {

    @Clone(transformer = TransformerWithDependencies.class, methodName = "newMethod")
    int method(int param1, String param2) {
        return param1 + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return newMethod(i(), s());
    }

}
