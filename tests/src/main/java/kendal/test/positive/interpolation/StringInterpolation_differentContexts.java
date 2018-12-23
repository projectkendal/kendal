package kendal.test.positive.interpolation;

import static kendal.test.utils.ValuesGenerator.i;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/*
 * @test
 * @summary check String Interpolation results for different interpolation contexts
 * @library /utils/
 * @build ValuesGenerator
 * @build AuxiliaryClass
 * @run testng kendal.test.positive.interpolation.StringInterpolation_differentContexts
 */
public class StringInterpolation_differentContexts {

    @Test
    public void shouldInterpolate_stringLiteral() {
        // GIVEN
        int someNumber = i();

        // WHEN
        String result = +"Variable value is: ${someNumber}";

        // THEN
        String expected = "Variable value is: " + someNumber;
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolate_methodParameter() {
        // GIVEN
        int someNumber = i();
        int param2 = i();

        // WHEN
        int result = someMethod(+"Variable value is: ${someNumber}", param2);

        // THEN
        String expected = someMethod("Variable value is: " + someNumber, param2);
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolate_constructorParameter() {
        // GIVEN
        int someNumber = i();
        int param2 = i();

        // WHEN
        AuxiliaryClass object = new AuxiliaryClass(+"Variable value is: ${someNumber}", param2);
        String result = object.getCreationResult();

        // THEN
        String expected = new AuxiliaryClass("Variable value is: " + someNumber, param2).getCreationResult();
        assertEquals(result, expected);
    }



    // Auxiliary stuff:

    String someMethod(String param1, int param2) {
        return param1 + " | " + param2;
    }

}
