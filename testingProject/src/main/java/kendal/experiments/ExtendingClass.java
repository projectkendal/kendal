package kendal.experiments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Public;

public class ExtendingClass extends TestClass implements Serializable {
    public ExtendingClass(int a, List<Integer> b, Object meineFinale) {
        super(a, b, meineFinale);
        lol = 15;
    }

    public ExtendingClass(Object meineFinale, @Public int lol) {
        super(meineFinale);
    }

    void someMethod() {
        TestClass testClass = new TestClass(69, new ArrayList<>(), null);
        int var = testClass.a;
        int var2 = testClass.x;
    }
}
