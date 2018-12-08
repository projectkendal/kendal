package kendal.test.positive.tsfields.publicModifier.otherPackage;

import java.util.ArrayList;
import java.util.List;

import kendal.test.positive.tsfields.publicModifier.ClassWithFieldsGenerated;

public class ExtendingClassInOtherPackage extends ClassWithFieldsGenerated {

    // ### Test cases ###

    // should generate "someNewField" field
    public ExtendingClassInOtherPackage(int primitiveField, List<Integer> listField, int primitiveFinalField,
            List<Integer> listFinalField/*, @Public boolean someNewField */) {
        super(primitiveField, listField, primitiveFinalField, listFinalField);
    }

    /* todo: fix adding field in subclasses
       todo: task - (https://trello.com/c/yP1LaqL5/36-tsfields-bug-fields-generation-in-subclasses-does-not-work)
    private boolean shouldAccessGeneratedField() {
        return someNewField;
    } */

    private int shouldAccessFieldFromSuperClassInOtherPackage_primitive_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessAndModifyFieldFromSuperClassInOtherPackage_primitive_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.primitiveFinalField = i();
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessFieldFromSuperClassInOtherPackage_list_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessAndModifyFieldFromSuperClassInOtherPackage_list_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.listField = l();
        return classWithFieldsGenerated.listField.size();
    }

    private int shouldAccessFieldFromSuperClassInOtherPackage_primitiveFinal_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessFieldFromSuperClassInOtherPackage_primitiveFinal_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessFieldFromSuperClassInOtherPackage_listFinal_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessFieldFromSuperClassInOtherPackage_listFinal_newClass() {
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
        shouldAccessFieldFromSuperClassInOtherPackage_primitive_identifier();
        shouldAccessAndModifyFieldFromSuperClassInOtherPackage_primitive_newClass();
        shouldAccessFieldFromSuperClassInOtherPackage_list_identifier();
        shouldAccessAndModifyFieldFromSuperClassInOtherPackage_list_newClass();
        shouldAccessFieldFromSuperClassInOtherPackage_primitiveFinal_identifier();
        shouldAccessFieldFromSuperClassInOtherPackage_primitiveFinal_newClass();
        shouldAccessFieldFromSuperClassInOtherPackage_listFinal_identifier();
        shouldAccessFieldFromSuperClassInOtherPackage_listFinal_newClass();
    }

}
