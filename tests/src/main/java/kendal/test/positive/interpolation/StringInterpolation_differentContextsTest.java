package kendal.test.positive.interpolation;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/*
 * @test
 * @summary check String Interpolation results for different interpolation contexts
 * @library /utils/
 * @build ValuesGenerator
 * @build AuxiliaryClass
 * @run testng kendal.test.positive.interpolation.StringInterpolation_differentContextsTest
 */
public class StringInterpolation_differentContextsTest {

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
        String result = someMethod(+"Variable value is: ${someNumber}", param2);

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

    @Test
    public void shouldInterpolate_stringConcatenationWithStringLHS() {
        // GIVEN
        String someString = s();
        int someNumber = i();

        // WHEN
        String result = +"Variable value is: ${someNumber}" + someString;

        // THEN
        String expected = ("Variable value is: " + someNumber) + someString;
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolate_stringConcatenationWithStringRHS() {
        // GIVEN
        String someString = s();
        int someNumber = i();

        // WHEN
        String result = someString + +"Variable value is: ${someNumber}";

        // THEN
        String expected = someString + "Variable value is: " + someNumber;
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolate_stringConcatenationWithIntLHS() {
        // GIVEN
        int someNumber1 = i();
        int someNumber2 = i();

        // WHEN
        String result = +"Variable value is: ${someNumber1}" + someNumber2;

        // THEN
        String expected = ("Variable value is: " + someNumber1) + someNumber2;
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolate_stringConcatenationWithIntRHS() {
        // GIVEN
        int someNumber1 = i();
        int someNumber2 = i();

        // WHEN
        String result = someNumber2 + +"Variable value is: ${someNumber1}";

        // THEN
        String expected = someNumber2 + "Variable value is: " + someNumber1;
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolate_parenthesis() {
        // GIVEN
        int someNumber = i();

        // WHEN
        String result = (+"Variable value is: ${someNumber}");

        // THEN
        String expected = "Variable value is: " + someNumber;
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolate_return() {
        // GIVEN
        int someNumber = i();

        // WHEN
        String result = methodReturningInterpolatedString(someNumber);

        // THEN
        String expected = "Variable value is: " + someNumber;
        assertEquals(result, expected);
    }



    // Auxiliary stuff:

    String someMethod(String param1, int param2) {
        return param1 + " | " + param2;
    }

    String methodReturningInterpolatedString(int value) {
        return +"Variable value is: ${value}";
    }


}
