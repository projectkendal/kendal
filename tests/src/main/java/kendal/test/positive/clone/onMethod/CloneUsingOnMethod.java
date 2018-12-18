package kendal.test.positive.clone.onMethod;

import kendal.annotations.Clone;
import org.testng.annotations.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.testng.Assert.*;

/*
 * @test
 * @summary check if class compiles and annotations passed using onMethod parameter of Clone are actually present on generated method
 * @build TestTransformer
 * @build TestAnnotation
 * @run testng kendal.test.positive.clone.onMethod.CloneUsingOnMethod
 */

public class CloneUsingOnMethod {

    private static final String value = "WRGH$#QT@#GW";

    @Clone(transformer = TestTransformer.class, methodName = "wanderlust",
            onMethod = {@TestAnnotation(value = value, classArray = {Class.class, CloneUsingOnMethod.class})})
    public String aMethod(String param, int anotherParam) {
        return "meine params: " + param + " " + anotherParam;
    }

    @Test
    public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = getClass().getDeclaredMethod("wanderlust", String.class, int.class);
        TestAnnotation annotation = method.getAnnotation(TestAnnotation.class);
        assertNotNull(annotation);
        assertEquals(annotation.value(), value);
        assertEqualsNoOrder(annotation.classArray(),
                new Class[] {Class.class, CloneUsingOnMethod.class},
                "classArray not equal to what it should be!");
        StringBuilder stringBuilder = (StringBuilder) method.invoke(this,"param", 43);
        assertEquals(stringBuilder.toString(), "meine params: param 43");
    }
}
