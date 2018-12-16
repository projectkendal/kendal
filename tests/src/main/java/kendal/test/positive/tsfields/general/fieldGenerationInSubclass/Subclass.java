package kendal.test.positive.tsfields.general.fieldGenerationInSubclass;

import java.util.List;

import kendal.annotations.PackagePrivate;
import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;

/*
 * @test
 * @build ClassWithFieldsGenerated
 * @compile Subclass.java
 */
@SuppressWarnings("unused")
public class Subclass extends ClassWithFieldsGenerated {

    Subclass(int superField1, List<Integer> superField2, int superField3, List<Integer> superField4,
            @Public int publicField, @Protected List protectedField,
            @PackagePrivate(makeFinal = false) int packagePrivateField, @Private(makeFinal = false) List privateField) {
        super(superField1, superField2, superField3, superField4);
    }


    // ### Compilation - Test cases ###

    private int shouldAccessGeneratedField() {
        return publicField + protectedField.size() + packagePrivateField + privateField.size();
    }
}
