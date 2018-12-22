package kendal.test.positive.clone.indirectAnnotation;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

/*
 * @test
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @build AuxiliaryAnnotation
 * @compile IndirectAnnotation.java IndirectAnnotationTest.java
 */
@SuppressWarnings("unused")
public class IndirectAnnotationTest {

    @IndirectAnnotation
    public String aMethod(String param, int anotherParam) {
        return "params: " + param + " " + anotherParam;
    }



    // ### Compilation - Test cases ###

    String shouldBeAbleToUseGeneratedMethod() {
        return generatedMethod(s(), i()).toString();
    }
}
