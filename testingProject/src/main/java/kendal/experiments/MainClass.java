package kendal.experiments;

import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main(String[] args) {
        TestClass testClass = new TestClass(69, new ArrayList<>(), null);
        CloneTest cloneTest = new CloneTest();
        List var = testClass.b;
        int var2 = testClass.a;
        testClass.b = new ArrayList<>();
        System.out.println(var2);
        System.out.println(cloneTest.transformedMethod("text", 123));
    }
}
