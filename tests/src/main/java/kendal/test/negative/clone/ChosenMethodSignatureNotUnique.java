package kendal.test.negative.clone;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @summary generated method with custom name - signature equal to a existing method signature
 * @build TestTransformer
 * @compile/fail/ref=ChosenMethodSignatureNotUnique.out ChosenMethodSignatureNotUnique.java -XDrawDiagnostics
 */
@SuppressWarnings("unused")
public class ChosenMethodSignatureNotUnique {

    @Clone(transformer = TestTransformer.class, methodName = "youWereTheChosenOne")
    public int method() {
        return 1;
    }

    public List<Integer> youWereTheChosenOne() {
        return new ArrayList<>();
    }
}
