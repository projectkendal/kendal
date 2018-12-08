package kendal.test.negative.tsfields;

import kendal.annotations.Private;

public class FinalFieldUninit {

    public FinalFieldUninit(@Private String aFinalField) {
        this.aFinalField = aFinalField; // uninit, because assignment is already generated, by definition of @Private.
    }

}
