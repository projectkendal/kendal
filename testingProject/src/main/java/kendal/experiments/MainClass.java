package kendal.experiments;

import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main() {
        TestClass testClass = new TestClass(69, new ArrayList<>(), null);
        List var = testClass.b;
        int var2 = testClass.a;
        testClass.b = new ArrayList<>();
    }
}
