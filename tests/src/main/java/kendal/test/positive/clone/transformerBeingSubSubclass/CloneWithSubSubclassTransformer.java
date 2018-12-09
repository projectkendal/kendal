package kendal.test.positive.clone.transformerBeingSubSubclass;

import static kendal.test.positive.utils.ValuesGenerator.i;
import static kendal.test.positive.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @library /positive/utils/
 * @build ValuesGenerator
 * @build SuperSuperTransformer
 * @build SuperTransformer
 * @build TestTransformer
 * @compile CloneWithSubSubclassTransformer.java
 */
@SuppressWarnings("unused")
public class CloneWithSubSubclassTransformer {

    @Clone(transformer = TestTransformer.class)
    int method(int param1, String param2) {
        return param1 + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(i(), s());
    }
}
