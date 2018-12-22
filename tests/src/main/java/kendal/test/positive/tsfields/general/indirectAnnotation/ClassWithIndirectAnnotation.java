package kendal.test.positive.tsfields.general.indirectAnnotation;

import static kendal.test.utils.ValuesGenerator.i;

/*
 * @test
 * @library /utils/
 * @build ValuesGenerator
 * @compile PrivateNotFinal.java ClassWithIndirectAnnotation.java
 */
@SuppressWarnings("unused")
class ClassWithIndirectAnnotation {

    ClassWithIndirectAnnotation(@PrivateNotFinal int primitiveField) { }

    // ### Compilation - Test cases ###

    private int shouldAccessAndModifyField_primitive_viaIdentifier() {
        ClassWithIndirectAnnotation classWithFieldsGenerated = new ClassWithIndirectAnnotation(i());
        classWithFieldsGenerated.primitiveField = i() + 1;
        return classWithFieldsGenerated.primitiveField;
    }
}
