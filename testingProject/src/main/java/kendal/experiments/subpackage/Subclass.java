package kendal.experiments.subpackage;

import java.util.ArrayList;

import kendal.experiments.TestClass;

public class Subclass {
    void someMethod() {
        TestClass testClass = new TestClass(69, new ArrayList<>(), null);
        int var = testClass.a;
    }
}
