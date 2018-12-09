package kendal.test.negative.interpolation;

/*
 * @test
 * @summary Using undefined variable inside expression of interpolated string should indicate invalid variable reference
 * @compile/fail/ref=InvalidVarReference.out InvalidVarReference.java -XDrawDiagnostics
 */
@SuppressWarnings("unused")
public class InvalidVarReference {

    void methodReferingToMissingVar() {
        System.out.println(+"${missingVariable}");
    }
}
