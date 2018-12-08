package kendal.test.positive.tsfields;

import kendal.annotations.Protected;
import kendal.annotations.Public;

import java.util.ArrayList;
import java.util.List;

public class GeneratedFieldsUsedWithinClass {
    private int x;

    public GeneratedFieldsUsedWithinClass(@Public(makeFinal = false) int publicPrimitiveInt,
                                          @Public(makeFinal = false) List<Integer> publicIntList,
                                          @Protected Object protectedFinalObject) {
        this.x = publicPrimitiveInt;
        System.out.println("Let's have here some expression statement");
    }

    private int someMethod() {
        return publicPrimitiveInt;
    }

    private int IdenticalMethodMethod() {
        return this.publicPrimitiveInt;
    }

    private void methodUsingNonPrimitiveObject() {
        publicIntList.add(x);
        publicIntList.add(publicPrimitiveInt);
    }

    private String testMethod() {
        return String.valueOf(new GeneratedFieldsUsedWithinClass(10, new ArrayList<>(), "xD").protectedFinalObject);
    }
}
