package kendal.test.positive.tsfields.general.inheritedAnnotation;

import static kendal.test.utils.ValuesGenerator.i;

/*
 * @test
 * @summary checks if fields are generated when inheriting annotation is used
 * @library /utils/
 * @build ValuesGenerator
 * @compile PrivateNotFinal.java ClassWithInheritedAnnotation.java
 */
@SuppressWarnings("unused")
class ClassWithInheritedAnnotation {

    ClassWithInheritedAnnotation(@PrivateNotFinal int primitiveField) { }

    // ### Compilation - Test cases ###

    private int shouldAccessAndModifyField_primitive_viaIdentifier() {
        ClassWithInheritedAnnotation classWithFieldsGenerated = new ClassWithInheritedAnnotation(i());
        classWithFieldsGenerated.primitiveField = i() + 1;
        return classWithFieldsGenerated.primitiveField;
    }
}
