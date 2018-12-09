package kendal.test.positive.tsfields.protectedModifier.otherPackage;

import java.util.ArrayList;
import java.util.List;

import kendal.test.positive.tsfields.protectedModifier.ClassWithFieldsGenerated;

@SuppressWarnings("unused")
public class ExtendingClassInOtherPackage extends ClassWithFieldsGenerated {

    // ### Test cases ###

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



    // ### utils ###
    private int i() {
        return 1;
    }

    private List<Integer> l() {
        return new ArrayList<>();
    }

}
