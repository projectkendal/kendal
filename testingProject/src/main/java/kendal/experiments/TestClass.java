package kendal.experiments;

import kendal.annotations.Private;
import kendal.annotations.Protected;
import kendal.annotations.Public;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class TestClass {
    int x;

    public TestClass(@Private(makeFinal = false) int a, @Public(makeFinal = false) List<Integer> b, @Protected Object meineFinale) {
        this.x = a;
        System.out.println("Let's have here some expression statement");
    }

    public TestClass(@Protected Object meineFinale) {

    }

    private int someMethod() {
        int var = a;
        a = 15;
        return a;
    }
}
