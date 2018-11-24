package kendal.experiments.subpackage;

import kendal.experiments.TestClass;

import java.util.ArrayList;
import java.util.List;

public class ExtendingClassInOtherPackage extends TestClass {
    public ExtendingClassInOtherPackage(int a, List<Integer> b, Object meineFinale) {
        super(a, b, meineFinale);
    }

    public ExtendingClassInOtherPackage(Object meineFinale) {
        super(meineFinale);
    }

    void someMethod() {
        TestClass testClass = new TestClass(69, new ArrayList<>(), null);
        int var = testClass.a;
    }
}
