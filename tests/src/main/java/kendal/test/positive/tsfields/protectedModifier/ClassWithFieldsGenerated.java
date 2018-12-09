package kendal.test.positive.tsfields.protectedModifier;

import static kendal.test.positive.utils.ValuesGenerator.i;
import static kendal.test.positive.utils.ValuesGenerator.l;

import java.util.List;

import kendal.annotations.Protected;

/*
 * @test
 * @library /positive/utils/
 * @build ValuesGenerator
 * @compile ClassWithFieldsGenerated.java
 */
@SuppressWarnings("unused")
public class ClassWithFieldsGenerated {

    public ClassWithFieldsGenerated(@Protected(makeFinal = false) int primitiveField,
            @Protected(makeFinal = false) List<Integer> listField,
            @Protected int primitiveFinalField,
            @Protected List<Integer> listFinalField) { }

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
