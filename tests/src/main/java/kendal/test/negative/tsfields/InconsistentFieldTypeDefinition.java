package kendal.test.negative.tsfields;

import kendal.annotations.Private;

import java.util.List;
import java.util.Map;

/*
 * @test
 * @summary Redefinition of generated field with different type.
 * @compile/fail/ref=InconsistentFieldTypeDefinition.out InconsistentFieldTypeDefinition.java
 */
public class InconsistentFieldTypeDefinition {

    InconsistentFieldTypeDefinition(@Private Integer fieldA) {
    }

    InconsistentFieldTypeDefinition(@Private String fieldA, @Private(makeFinal = false)Map<String, List> fieldB) {
    }
}
