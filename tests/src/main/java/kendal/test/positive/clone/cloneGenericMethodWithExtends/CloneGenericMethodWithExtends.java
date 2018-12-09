package kendal.test.positive.clone.cloneGenericMethodWithExtends;

import static kendal.test.utils.ValuesGenerator.i;
import static kendal.test.utils.ValuesGenerator.s;

import java.util.Collection;
import java.util.List;

import kendal.annotations.Clone;

/*
 * @test
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneGenericMethodWithExtends.java
 */
@SuppressWarnings("unused")
public class CloneGenericMethodWithExtends {

    @Clone(transformer = TestTransformer.class)
    <T extends Collection> int method(T param1, String param2) {
        return param1.size() + param2.length();
    }



    // ### Compilation - Test cases ###

    /* todo: fix adding field in subclasses
       todo: task - (https://trello.com/c/bmkauaTx/32-clonebug-clone-does-not-work-with-generic-methods) */
    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(i(), s());
    }

}
