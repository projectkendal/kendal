package kendal.test.positive.tsfields.packagePrivateModifier;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.l;

/*
 * @test
 * @summary check if packagePrivate fields are properly generated and available within class in the same package.
 * @library /utils/
 * @build ValuesGenerator
 * @build ClassWithFieldsGenerated
 * @compile ClassInTheSamePackage.java
 */
@SuppressWarnings("unused")
public class ClassInTheSamePackage {

    // ### Compilation - Test cases ###

    private int shouldAccessField_primitive_viaNewClass() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessAndModifyField_primitive_viaIdentifier() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.primitiveField = i();
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessField_list_viaNewClass() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessAndModifyField_list_viaIdentifier() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.listField = l();
        return classWithFieldsGenerated.listField.size();
    }

    private int shouldAccessField_primitiveFinal_viaNewClass() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessField_primitiveFinal_viaIdentifier() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessField_listFinal_viaNewClass() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessField_listFinal_viaIdentifier() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        return classWithFieldsGenerated.listField.size();
    }

}
