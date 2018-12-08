package kendal.test.negative.tsfields;

import kendal.annotations.Private;
import kendal.annotations.Protected;

import java.util.List;
import java.util.Map;

public class InconsistentFieldAccessModifierDefinition {

    InconsistentFieldAccessModifierDefinition(@Private Integer fieldA) {
    }
    // fieldA redefined with different access
    InconsistentFieldAccessModifierDefinition(@Protected Integer fieldA, @Private(makeFinal = false)Map<String, List> fieldB) {
    }
}
