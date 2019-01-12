package kendal.test.positive.clone.naming;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check whether created clone method has proper name when it is defined using string literal
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneWithLiteralAsNameTest.java
 */
@SuppressWarnings("unused")
class CloneWithLiteralAsNameTest {

    @Clone(transformer = TestTransformer.class, methodName = "newMethod")
    int method(int param1, String param2) {
        return param1 + param2.length();
    }


    
    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return newMethod(i(), s());
    }
}
