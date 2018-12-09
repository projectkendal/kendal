package kendal.test.negative.clone;

import kendal.annotations.Clone;

/*
 * @test
 * @summary clone method name invalid
 * @build TestTransformer
 * @compile/fail/ref=CloneMethodNameInvalid.out CloneMethodNameInvalid.java
 */
@SuppressWarnings("unused")
public class CloneMethodNameInvalid {

    @Clone(transformer = TestTransformer.class, methodName = "!#@%@#%@%#")
    public Integer method() {
        return null;
    }
}
