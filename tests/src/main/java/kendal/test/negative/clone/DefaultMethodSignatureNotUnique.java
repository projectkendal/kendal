package kendal.test.negative.clone;

import kendal.annotations.Clone;

/*
 * @test
 * @summary generated method with default name - signature equal to a existing method signature
 * @build TestTransformer
 * @compile/fail/ref=DefaultMethodSignatureNotUnique.out DefaultMethodSignatureNotUnique.java -XDrawDiagnostics
 */
public class DefaultMethodSignatureNotUnique {

    @Clone(transformer = TestTransformer.class)
    public int method() {
        return 1;
    }

    public int methodClone() {
        return 1;
    }
}
