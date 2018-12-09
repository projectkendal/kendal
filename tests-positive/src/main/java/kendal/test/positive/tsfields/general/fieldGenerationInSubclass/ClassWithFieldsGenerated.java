package kendal.test.positive.tsfields.general.fieldGenerationInSubclass;

import java.util.List;

import kendal.annotations.PackagePrivate;
import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;

@SuppressWarnings("unused")
class ClassWithFieldsGenerated {

    ClassWithFieldsGenerated(@Public(makeFinal = false) int primitiveField,
            @Protected(makeFinal = false) List<Integer> listField,
            @PackagePrivate int primitiveFinalField,
            @Private List<Integer> listFinalField) { }

}
