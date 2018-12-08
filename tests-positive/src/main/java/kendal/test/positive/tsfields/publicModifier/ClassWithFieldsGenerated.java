package kendal.test.positive.tsfields.publicModifier;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Public;

public class ClassWithFieldsGenerated {

    protected int x;

    public ClassWithFieldsGenerated(@Public(makeFinal = false) int primitiveField,
            @Public(makeFinal = false) List<Integer> listField,
            @Public(makeFinal = false) int primitiveFinalField,
            @Public List<Integer> listFinalField) { }

    // ### Test cases ###

    private int shouldAccessField_primitive_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessAndModifyField_primitive_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.primitiveFinalField = i();
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessField_list_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessAndModifyField_list_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        classWithFieldsGenerated.listField = l();
        return classWithFieldsGenerated.listField.size();
    }

    private int shouldAccessField_primitiveFinal_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).primitiveField;
    }

    private int shouldAccessField_primitiveFinal_newClass() {
        ClassWithFieldsGenerated classWithFieldsGenerated = new ClassWithFieldsGenerated(i(), l(), i(), l());
        return classWithFieldsGenerated.primitiveField;
    }

    private int shouldAccessField_listFinal_identifier() {
        return new ClassWithFieldsGenerated(i(), l(), i(), l()).listField.size();
    }

    private int shouldAccessField_listFinal_newClass() {
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
        shouldAccessField_primitive_identifier();
        shouldAccessAndModifyField_primitive_newClass();
        shouldAccessField_list_identifier();
        shouldAccessAndModifyField_list_newClass();
        shouldAccessField_primitiveFinal_identifier();
        shouldAccessField_primitiveFinal_newClass();
        shouldAccessField_listFinal_identifier();
        shouldAccessField_listFinal_newClass();
    }

}
