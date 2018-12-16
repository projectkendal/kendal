package kendal.test.positive.clone.cloneMethodWithGenericArgWithExtends;

import static kendal.test.utils.ValuesGenerator.l;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.Collection;
import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneMethodWithGenericArgWithExtends.java
 */
@SuppressWarnings("unused")
public class CloneMethodWithGenericArgWithExtends {

    @Clone(transformer = TestTransformer.class)
    <T extends Collection> int method(T param1, String param2) {
        return param1.size() + param2.length();
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(l(), s());
    }

}
