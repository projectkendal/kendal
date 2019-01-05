package kendal.test.positive.clone.indirectAnnotation;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

/*
 * @test
 * @summary check whether clone for method is created properly when using inheritance mechanism
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @build AuxiliaryAnnotation
 * @compile InheritingAnnotation.java InheritingAnnotationTest.java
 */
@SuppressWarnings("unused")
public class InheritingAnnotationTest {

    @InheritingAnnotation
    public String aMethod(String param, int anotherParam) {
        return "params: " + param + " " + anotherParam;
    }



    // ### Compilation - Test cases ###

    String shouldBeAbleToUseGeneratedMethod() {
        return generatedMethod(s(), i()).toString();
    }
}
