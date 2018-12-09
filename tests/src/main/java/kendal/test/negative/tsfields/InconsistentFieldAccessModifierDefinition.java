package kendal.test.negative.tsfields;

import java.util.List;
import java.util.Map;

import kendal.annotations.Private;
import kendal.annotations.Protected;

/*
 * @test
 * @summary Redefinition of generated field with different access modifier.
 * @compile/fail/ref=InconsistentFieldAccessModifierDefinition.out InconsistentFieldAccessModifierDefinition.java -XDrawDiagnostics
 */
@SuppressWarnings("unused")
public class InconsistentFieldAccessModifierDefinition {

    InconsistentFieldAccessModifierDefinition(@Private Integer fieldA) {
    }

    InconsistentFieldAccessModifierDefinition(@Protected Integer fieldA, @Private(makeFinal = false)Map<String, List> fieldB) {
    }
}
