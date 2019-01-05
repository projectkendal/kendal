package kendal.test.positive.clone.cloneMethodWithGenericReturn;

/*
 * @//test
 * @summary check whether clone for method with generic return type is created properly
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneMethodWithGenericReturn.java
 */
@SuppressWarnings("unused")
public class CloneMethodWithGenericReturn {

     /*
    todo: https://trello.com/c/jgLmn2pe/50-clone-clone-methods-with-generic-return-type
    @Clone(transformer = TestTransformer.class)
    <T> T method(T param1, String param2) {
        return param1;
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(new SomeType(), s());
    }

    private class SomeType { }
    */
}
