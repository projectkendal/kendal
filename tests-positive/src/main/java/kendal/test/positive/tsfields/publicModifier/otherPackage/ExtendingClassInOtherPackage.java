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

    private int shouldAccessFieldFromOtherClassInTheSamePackage_primitive_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessAndModifyFieldFromOtherClassInTheSamePackage_primitive_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.primitiveFinalField = i();
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessFieldFromOtherClassInTheSamePackage_list_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessAndModifyFieldFromOtherClassInTheSamePackage_list_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.listField = l();
        return classWithFieldsGenerated.listField.size();
    }

    private int shouldAccessFieldFromOtherClassInTheSamePackage_primitiveFinal_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessFieldFromOtherClassInTheSamePackage_primitiveFinal_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessFieldFromOtherClassInTheSamePackage_listFinal_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessFieldFromOtherClassInTheSamePackage_listFinal_newClass() {
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

    // ### use methods to make methods look like they are used (yellow color instead of grey)
    private void useMethods() {
        shouldAccessFieldFromOtherClassInTheSamePackage_primitive_identifier();
        shouldAccessAndModifyFieldFromOtherClassInTheSamePackage_primitive_newClass();
        shouldAccessFieldFromOtherClassInTheSamePackage_list_identifier();
        shouldAccessAndModifyFieldFromOtherClassInTheSamePackage_list_newClass();
        shouldAccessFieldFromOtherClassInTheSamePackage_primitiveFinal_identifier();
        shouldAccessFieldFromOtherClassInTheSamePackage_primitiveFinal_newClass();
        shouldAccessFieldFromOtherClassInTheSamePackage_listFinal_identifier();
        shouldAccessFieldFromOtherClassInTheSamePackage_listFinal_newClass();
    }

}
