package kendal.experiments;

import java.util.List;

import kendal.annotations.Protected;
import kendal.annotations.Public;
import kendal.api.inheritance.Inherit;

public class TestClass {
    protected int x;

    public TestClass(@NotFinalPublic int a, @Public(makeFinal = false) List<Integer> b, @Protected Object meineFinale) {
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

    @Inherit(@Public(makeFinal = false))
    private @interface NotFinalPublic { }
}
