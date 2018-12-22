package kendal.test.positive.tsfields.general.indirectAnnotationDepthTwo;

import static kendal.test.utils.ValuesGenerator.i;

/*
 * @test
 * @library /utils/
 * @build ValuesGenerator
 * @compile PrivateNotFinalDepthOne.java PrivateNotFinalDepthTwo.java ClassWithIndirectAnnotationDepthTwo.java
 */
@SuppressWarnings("unused")
class ClassWithIndirectAnnotationDepthTwo {

    ClassWithIndirectAnnotationDepthTwo(@PrivateNotFinalDepthTwo int primitiveField) { }

    // ### Compilation - Test cases ###

    private int shouldAccessAndModifyField_primitive_viaIdentifier() {
        ClassWithIndirectAnnotationDepthTwo classWithFieldsGenerated = new ClassWithIndirectAnnotationDepthTwo(i());
        classWithFieldsGenerated.primitiveField = i() + 1;
        return classWithFieldsGenerated.primitiveField;
    }
}
