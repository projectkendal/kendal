package kendal.experiments;

import kendal.annotations.Private;
import kendal.annotations.Protected;

public class TestClass {
    int x;

    public TestClass(@Private int a, @Protected String b) {

        this.x = a;
        System.out.println("Let's have here some expression statement");
    }
}
