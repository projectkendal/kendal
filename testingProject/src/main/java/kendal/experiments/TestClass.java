package kendal.experiments;

import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;

import java.util.Map;
import java.util.Set;

public class TestClass {
    int x;

    public TestClass(@Private(makeFinal = false) int a, @Public(makeFinal = false) int b, @Protected(makeFinal = true) int meineFinale) {
        this.x = a;
        System.out.println("Let's have here some expression statement");
    }

    private int someMethod() {
        int var = a;
        a = 15;
        return a;
    }
}
