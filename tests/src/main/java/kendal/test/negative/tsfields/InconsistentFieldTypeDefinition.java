package kendal.test.negative.tsfields;

import java.util.List;
import java.util.Map;

import kendal.annotations.Private;

/*
 * @test
 * @summary Redefinition of generated field with different type.
 * @compile/fail/ref=InconsistentFieldTypeDefinition.out InconsistentFieldTypeDefinition.java
 */
@SuppressWarnings("unused")
public class InconsistentFieldTypeDefinition {

    InconsistentFieldTypeDefinition(@Private Integer fieldA) {
    }

    InconsistentFieldTypeDefinition(@Private String fieldA, @Private(makeFinal = false)Map<String, List> fieldB) {
    }
}
