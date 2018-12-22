package kendal.test.positive.clone.IndirectAnnotationDepthTwo;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

/*
 * @test
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @build AuxiliaryAnnotation
 * @compile IndirectAnnotationDepthOne.java IndirectAnnotationDepthTwo.java IndirectAnnotationDepthTwoTest.java
 */
@SuppressWarnings("unused")
public class IndirectAnnotationDepthTwoTest {

    @IndirectAnnotationDepthTwo
    public String aMethod(String param, int anotherParam) {
        return "params: " + param + " " + anotherParam;
    }



    // ### Compilation - Test cases ###

    String shouldBeAbleToUseGeneratedMethod() {
        return generatedMethod(s(), i()).toString();
    }
}
