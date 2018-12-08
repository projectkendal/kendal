package kendal.test.negative.interpolation;

public class InvalidVarReference {

    void methodReferingToMissingVar() {
        System.out.println(+"${missingVariable}");
    }
}
