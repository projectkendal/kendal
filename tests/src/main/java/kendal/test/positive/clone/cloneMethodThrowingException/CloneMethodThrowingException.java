package kendal.test.positive.clone.cloneMethodThrowingException;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneMethodThrowingException.java
 */
@SuppressWarnings("unused")
public class CloneMethodThrowingException {

    @Clone(transformer = TestTransformer.class)
    int method(int param1, String param2) throws Exception {
        if (param1 == 12) throw new Exception("Some exception!");
        return param1 + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() throws Exception {
        return methodClone(i(), s());
    }
}
