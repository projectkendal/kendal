package kendal.test.positive.interpolation;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
/*
 * @test
 * @summary check interpolated strings equality with the expected values
 * @run testng kendal.test.positive.interpolation.Test1
 */
@Test
public class Test1 {
    private static final int numberField = 45;
    private final String interpolatedField = +"whatever ${numberField}";

    private String interpolatedReturn() {
        return +"value ${interpolatedField}";
    }

    private String identity(String value) {
        return value;
    }

    public void testValuesEqual() {
        assertEquals(interpolatedField, "whatever 45");
        assertEquals(interpolatedReturn(), "value whatever 45");
        assertEquals(identity(+"value ${interpolatedField}"), "value whatever 45" );
    }
}
