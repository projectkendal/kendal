package kendal.experiments;

import kendal.annotations.Private;
import kendal.annotations.Public;

public class TestClass {
    int x;

    public TestClass(@Private int a, @Public int b) {
        this.x = a;
        System.out.println("Let's have here some expression statement");
    }

    private int someMethod() {
        a = 15;
        return a;
    }
}
