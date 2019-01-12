package kendal.test.negative.tsfields;

import kendal.annotations.Private;

/*
 * @test
 * @summary This kind of construct indicates that user does not understand how Private works.
 * field is generated automatically, so having it defined manually as well misses the point of using annotation.
 * We signal an error.
 * @compile/fail/ref=ExistingFieldRedefinition.out ExistingFieldRedefinition.java -XDrawDiagnostics
 */
@SuppressWarnings("unused")
public class ExistingFieldRedefinition {

    Object field;

    ExistingFieldRedefinition(@Private Object field) {
    }
}
