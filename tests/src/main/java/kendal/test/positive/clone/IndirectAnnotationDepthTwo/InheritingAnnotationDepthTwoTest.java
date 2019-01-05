package kendal.test.positive.clone.IndirectAnnotationDepthTwo;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

/*
 * @test
 * @summary check whether clone for method is created properly when using inheritance mechanism of depth 2
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @build AuxiliaryAnnotation
 * @compile IndirectAnnotationDepthOne.java InheritingAnnotationDepthTwo.java InheritingAnnotationDepthTwoTest.java
 */
@SuppressWarnings("unused")
public class InheritingAnnotationDepthTwoTest {

    @InheritingAnnotationDepthTwo
    public String aMethod(String param, int anotherParam) {
        return "params: " + param + " " + anotherParam;
    }



    // ### Compilation - Test cases ###

    String shouldBeAbleToUseGeneratedMethod() {
        return generatedMethod(s(), i()).toString();
    }
}
