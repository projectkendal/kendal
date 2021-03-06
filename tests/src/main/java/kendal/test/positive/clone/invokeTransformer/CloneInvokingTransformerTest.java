package kendal.test.positive.clone.invokeTransformer;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;
import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check whether created clone method actually invokes transformer specified as the attribute for the annotation
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @run testng kendal.test.positive.clone.invokeTransformer.CloneInvokingTransformerTest
 */
@SuppressWarnings("unused")
public class CloneInvokingTransformerTest {

    @Clone(transformer = TestTransformer.class, methodName = "newMethod")
    int method(int param1, String param2) {
        return param1 + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return newMethod(i(), s());
    }



    // ### Runtime - Test cases ###
    private static final int INPUT_INT = 10;
    private static final String INPUT_STRING = "123";

    @Test
    public void shouldReturnTransformedValue() {
        // GIVEN
        Integer result = method(INPUT_INT, INPUT_STRING);
        int expectedResult = result + 1;

        // WHEN
        List<Integer> transformedResult = newMethod(INPUT_INT, INPUT_STRING);

        // THEN
        int value = transformedResult.get(0);
        assertEquals(transformedResult.size(), 1);
        assertEquals(expectedResult, value);
    }

}
