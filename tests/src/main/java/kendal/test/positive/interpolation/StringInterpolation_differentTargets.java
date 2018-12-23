package kendal.test.positive.interpolation;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/*
 * @test
 * @summary check String Interpolation results for different interpolation targets
 * @library /utils/
 * @build ValuesGenerator
 * @build AuxiliaryClass
 * @run testng kendal.test.positive.interpolation.StringInterpolation_differentTargets
 */
public class StringInterpolation_differentTargets {

    @Test
    public void shouldInterpolateVariableValue_primitiveType() {
        // GIVEN
        int someNumber = i();

        // WHEN
        String result = +"Variable value is: ${someNumber}";

        // THEN
        String expected = "Variable value is: " + someNumber;
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolateVariableValue_complexType() {
        // GIVEN
        AuxiliaryClass someObject = new AuxiliaryClass();

        // WHEN
        String result = +"Variable value is: ${someObject}";

        // THEN
        String expected = "Variable value is: " + someObject.toString();
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolateMethodValue_primitiveType() {
        // GIVEN
        String param1 = s();
        int param2 = i();

        // WHEN
        String result = +"Method value is: ${primitiveMethod(param1, param2)}";

        // THEN
        String expected = "Method value is: " + primitiveMethod(param1, param2);
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolateMethodValue_primitiveStaticType() {
        // GIVEN
        String param1 = s();
        int param2 = i();

        // WHEN
        String result = +"Method value is: ${primitiveStaticMethod(param1, param2)}";

        // THEN
        String expected = "Method value is: " + primitiveStaticMethod(param1, param2);
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolateMethodValue_complexType() {
        // GIVEN
        String param1 = s();
        int param2 = i();

        // WHEN
        String result = +"Method value is: ${complexMethod(param1, param2)}";

        // THEN
        String expected = "Method value is: " + complexMethod(param1, param2);
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolateMethodValue_complexStaticType() {
        // GIVEN
        String param1 = s();
        int param2 = i();

        // WHEN
        String result = +"Method value is: ${complexStaticMethod(param1, param2)}";

        // THEN
        String expected = "Method value is: " + complexStaticMethod(param1, param2);
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolateNewObjectValue() {
        // GIVEN

        // WHEN
        String result = +"New object value is: ${new AuxiliaryClass()}";

        // THEN
        String expected = "New object value is: " + new AuxiliaryClass();
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolateMathOperationsValue() {
        // GIVEN
        int x = 7;
        int y = 13;
        int z = 17;

        // WHEN
        String result = +"Calculated value is: ${x + y * z - 19}";

        // THEN
        String expected = "Calculated value is: " + (x + y * z - 19);
        assertEquals(result, expected);
    }

    @Test
    public void shouldInterpolateStringConcatenationValue() {
        // GIVEN
        String str = "11";
        int x = 13;

        // WHEN
        String result = +"Calculated value is: ${str + x}";

        // THEN
        String expected = "Calculated value is: " + (str + x);
        assertEquals(result, expected);
    }



    // Auxiliary stuff:

    int primitiveMethod(String param1, int param2) {
        return (param1 + " | " + param2).length();
    }

    static int primitiveStaticMethod(String param1, int param2) {
        return (param1 + " & " + param2).length();
    }

    String complexMethod(String param1, int param2) {
        return param1 + " ^ " + param2;
    }

    static String complexStaticMethod(String param1, int param2) {
        return param1 + " % " + param2;
    }

}
