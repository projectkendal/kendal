package kendal.test.positive.clone.naming;

import static kendal.test.positive.utils.ValuesGenerator.i;
import static kendal.test.positive.utils.ValuesGenerator.s;

import java.util.List;

import kendal.annotations.Clone;

@SuppressWarnings("unused")
class CloneWithLiteralAsName {

    @Clone(transformer = TestTransformer.class, methodName = "newMethod")
    int method(int param1, String param2) {
        return param1 + param2.length();
    }

    // ### Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return newMethod(i(), s());
    }
}
