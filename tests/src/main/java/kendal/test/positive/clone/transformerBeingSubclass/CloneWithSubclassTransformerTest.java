package kendal.test.positive.clone.transformerBeingSubclass;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check whether clone method is properly created when transformer is a subclass of class implementing Transformer interface
 * @library /utils/
 * @build ValuesGenerator
 * @build SuperTransformer
 * @build TestTransformer
 * @compile CloneWithSubclassTransformerTest.java
 */
@SuppressWarnings("unused")
public class CloneWithSubclassTransformerTest {

    @Clone(transformer = TestTransformer.class)
    int method(int param1, String param2) {
        return param1 + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(i(), s());
    }
}
