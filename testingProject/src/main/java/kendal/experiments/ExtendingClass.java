package kendal.experiments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ExtendingClass extends TestClass implements Serializable {
    public ExtendingClass(int a, List<Integer> b, Object meineFinale) {
        super(a, b, meineFinale);
    }

    public ExtendingClass(Object meineFinale) {
        super(meineFinale);
    }

    void someMethod() {
        TestClass testClass = new TestClass(69, new ArrayList<>(), null);
        int var = testClass.a;
        int var2 = testClass.x;
    }
}
