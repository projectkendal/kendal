package kendal.test.negative.tsfields;

import kendal.annotations.Private;

import java.util.List;
import java.util.Map;

public class InconsistentFieldTypeDefinition {

    InconsistentFieldTypeDefinition(@Private Integer fieldA) {
    }
    // fieldA redefined with different type
    InconsistentFieldTypeDefinition(@Private String fieldA, @Private(makeFinal = false)Map<String, List> fieldB) {
    }
}
