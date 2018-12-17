package kendal.test.positive.interpolation;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
/*
 * @test
 * @summary check interpolated strings equality with the expected values
 * @run testng kendal.test.positive.interpolation.Test1
 */
public class Test1 {
    private static final int numberField = 45;
    private final String interpolatedField = +"whatever ${numberField}";

    private String interpolatedReturn() {
        return +"value ${interpolatedField}";
    }

    private String identity(boolean i, String value, int l) {
        return value;
    }

    @Test
    public void testValuesEqual() {
        assertEquals(interpolatedField, "whatever 45");
        assertEquals(interpolatedReturn(), "value whatever 45");
        assertEquals(identity(true, +"value ${interpolatedField}", 90), "value whatever 45");
    }
}
