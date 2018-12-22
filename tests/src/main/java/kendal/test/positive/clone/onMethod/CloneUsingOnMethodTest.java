package kendal.test.positive.clone.onMethod;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertEqualsNoOrder;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.testng.annotations.Test;

import kendal.annotations.Clone;

/*
 * @test
 * @summary check if class compiles and annotations passed using onMethod parameter of Clone are actually present on generated method
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @build AuxiliaryAnnotation
 * @run testng kendal.test.positive.clone.onMethod.CloneUsingOnMethodTest
 */

public class CloneUsingOnMethodTest {

    private static final String VALUE = "WRGH$#QT@#GW";

    @Clone(transformer = TestTransformer.class, methodName = "wanderlust",
            onMethod = {@AuxiliaryAnnotation(value = VALUE, classArray = {Class.class, CloneUsingOnMethodTest.class}, someFlag = false)})
    public String aMethod(String param, int anotherParam) {
        return "params: " + param + " " + anotherParam;
    }

    @Test
    public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // GIVEN
        String param1 = s();
        int param2 = i();

        // WHEN
        // compilation is performed

        // THEN
        Method method = getClass().getDeclaredMethod("wanderlust", String.class, int.class);
        AuxiliaryAnnotation annotation = method.getAnnotation(AuxiliaryAnnotation.class);
        assertNotNull(annotation);
        assertEquals(annotation.value(), VALUE);
        assertFalse(annotation.someFlag());
        assertEqualsNoOrder(annotation.classArray(),
                new Class[] {Class.class, CloneUsingOnMethodTest.class},
                "classArray not equal to what it should be!");
        StringBuilder stringBuilder = (StringBuilder) method.invoke(this,param1, param2);
        assertEquals(stringBuilder.toString(), "params: " + param1 + " " + param2 + "postfix");
    }
}
