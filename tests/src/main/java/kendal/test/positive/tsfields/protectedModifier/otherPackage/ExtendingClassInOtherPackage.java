package kendal.test.positive.tsfields.protectedModifier.otherPackage;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.l;

import java.util.List;

import kendal.test.positive.tsfields.protectedModifier.ClassWithFieldsGenerated;

/*
 * @test
 * @summary check if protected fields are properly generated and available within class in other package extending class with generated fields
 * @library /utils/
 * @library ../
 * @build ValuesGenerator
 * @build ClassWithFieldsGenerated
 * @compile ExtendingClassInOtherPackage.java
 */
@SuppressWarnings("unused")
public class ExtendingClassInOtherPackage extends ClassWithFieldsGenerated {

    // ### Compilation - Test cases ###

    // should generate "someNewField" field
    public ExtendingClassInOtherPackage(int primitiveField, List<Integer> listField, int primitiveFinalField,
            List<Integer> listFinalField) {
        super(primitiveField, listField, primitiveFinalField, listFinalField);
    }

    private int shouldAccessField_primitive_viaNewClass() {
        return new ExtendingClassInOtherPackage(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessAndModifyField_primitive_viaIdentifier() {
        ExtendingClassInOtherPackage ExtendingClassInOtherPackage = new ExtendingClassInOtherPackage(i(), l(), i(), l());
        ExtendingClassInOtherPackage.primitiveField = i();
        return ExtendingClassInOtherPackage.primitiveField;
    }

    private int shouldAccessField_list_viaNewClass() {
        return new ExtendingClassInOtherPackage(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessAndModifyField_list_viaIdentifier() {
        ExtendingClassInOtherPackage ExtendingClassInOtherPackage = new ExtendingClassInOtherPackage(i(), l(), i(), l());
        ExtendingClassInOtherPackage.listField = l();
        return ExtendingClassInOtherPackage.listField.size();
    }

    private int shouldAccessField_primitiveFinal_viaNewClass() {
        return new ExtendingClassInOtherPackage(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessField_primitiveFinal_viaIdentifier() {
        ExtendingClassInOtherPackage ExtendingClassInOtherPackage = new ExtendingClassInOtherPackage(i(), l(), i(), l());
        return ExtendingClassInOtherPackage.primitiveField;
    }

    private int shouldAccessField_listFinal_viaNewClass() {
        return new ExtendingClassInOtherPackage(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessField_listFinal_viaIdentifier() {
        ExtendingClassInOtherPackage ExtendingClassInOtherPackage = new ExtendingClassInOtherPackage(i(), l(), i(), l());
        return ExtendingClassInOtherPackage.listField.size();
    }

}
