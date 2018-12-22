package kendal.test.positive.clone.indirectAnnotation;

/*
 * @test
 * @library /utils/
 * @build TestTransformer
 * @build IndirectAnnotation
 * @compile IndirectAnnotationTest.java
 */
@SuppressWarnings("unused")
public class IndirectAnnotationTest {

    @IndirectAnnotation
    public String aMethod(String param, int anotherParam) {
        return "params: " + param + " " + anotherParam;
    }



    // ### Compilation - Test cases ###

    String shouldBeAbleToUseGeneratedMethod() {
        return aMethodClone("2", 324).toString();
    }
}
