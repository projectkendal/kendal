package kendal.test.positive.tsfields.publicModifier.otherPackage;

import java.util.ArrayList;
import java.util.List;

import kendal.test.positive.tsfields.publicModifier.ClassWithFieldsGenerated;

public class ClassInOtherPackage {

    // ### Test cases ###

    private int shouldAccessFieldFromOtherClassInOtherPackage_primitive_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessAndModifyFieldFromOtherClassInOtherPackage_primitive_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.primitiveFinalField = i();
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessFieldFromOtherClassInOtherPackage_list_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessAndModifyFieldFromOtherClassInOtherPackage_list_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.listField = l();
        return classWithFieldsGenerated.listField.size();
    }

    private int shouldAccessFieldFromOtherClassInOtherPackage_primitiveFinal_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessFieldFromOtherClassInOtherPackage_primitiveFinal_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessFieldFromOtherClassInOtherPackage_listFinal_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessFieldFromOtherClassInOtherPackage_listFinal_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        return classWithFieldsGenerated.listField.size();
    }



    // ### utils ###
    private int i() {
        return 1;
    }

    private List<Integer> l() {
        return new ArrayList<>();
    }

    // ### use methods to make methods look like they are used (yellow color instead of grey) ###
    private void useMethods() {
        shouldAccessFieldFromOtherClassInOtherPackage_primitive_identifier();
        shouldAccessAndModifyFieldFromOtherClassInOtherPackage_primitive_newClass();
        shouldAccessFieldFromOtherClassInOtherPackage_list_identifier();
        shouldAccessAndModifyFieldFromOtherClassInOtherPackage_list_newClass();
        shouldAccessFieldFromOtherClassInOtherPackage_primitiveFinal_identifier();
        shouldAccessFieldFromOtherClassInOtherPackage_primitiveFinal_newClass();
        shouldAccessFieldFromOtherClassInOtherPackage_listFinal_identifier();
        shouldAccessFieldFromOtherClassInOtherPackage_listFinal_newClass();
    }

}
