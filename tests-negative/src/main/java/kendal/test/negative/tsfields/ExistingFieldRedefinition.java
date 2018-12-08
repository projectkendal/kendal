package kendal.test.negative.tsfields;

import kendal.annotations.Private;

public class ExistingFieldRedefinition {

    Object field;

    // This kind of construct indicates that user does not understand how @Private works;
    // field is generated automatically, so having it defined manually as well misses the point of using annotation.
    // We signal an error.
    ExistingFieldRedefinition(@Private Object field) {
    }
}
