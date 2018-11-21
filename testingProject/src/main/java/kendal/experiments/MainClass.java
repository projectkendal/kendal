package kendal.experiments;

import java.util.ArrayList;
import java.util.List;

public class MainClass {

    public static void main() {
        TestClass testClass = new TestClass(69, null, null);
        List var = testClass.b;
        testClass.b = new ArrayList<>();
    }
}
