package kendal.test.positive.tsfields.general.inheritedAnnotationDepthTwo;

import static kendal.test.utils.ValuesGenerator.i;

/*
 * @test
 * @summary checks if fields are generated when inheriting annotation is used (inheritance depth = 2)
 * @library /utils/
 * @build ValuesGenerator
 * @compile PrivateNotFinalDepthOne.java PrivateNotFinalDepthTwo.java ClassWithInheritedAnnotationDepthTwo.java
 */
@SuppressWarnings("unused")
class ClassWithInheritedAnnotationDepthTwo {

    ClassWithInheritedAnnotationDepthTwo(@PrivateNotFinalDepthTwo int primitiveField) { }

    // ### Compilation - Test cases ###

    private int shouldAccessAndModifyField_primitive_viaIdentifier() {
        ClassWithInheritedAnnotationDepthTwo classWithFieldsGenerated = new ClassWithInheritedAnnotationDepthTwo(i());
        classWithFieldsGenerated.primitiveField = i() + 1;
        return classWithFieldsGenerated.primitiveField;
    }
}
