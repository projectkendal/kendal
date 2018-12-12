package kendal.test.negative.tsfields;

import kendal.annotations.Private;

/*
 * @test
 * @summary Failure in case of final variable reassignment, caused by user assigning value to final field generated
 * by Kendal. Generated fields have also generated assignment.
 * @compile/fail/ref=FinalFieldUninit.out FinalFieldUninit.java -XDrawDiagnostics
 */
public class FinalFieldUninit {

    public FinalFieldUninit(@Private String aFinalField) {
    }

    void reassignFinalField() {
        aFinalField = "sda";
    }
}
