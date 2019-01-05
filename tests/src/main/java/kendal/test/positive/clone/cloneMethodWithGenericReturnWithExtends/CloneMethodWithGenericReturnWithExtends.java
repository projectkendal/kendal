package kendal.test.positive.clone.cloneMethodWithGenericReturnWithExtends;

/*
 * @//test
 * @summary check whether clone for method with generic with extends return type is created properly
 * @library /utils/
 * @build ValuesGenerator
 * @build TestTransformer
 * @compile CloneMethodWithGenericReturnWithExtends.java
 */
@SuppressWarnings("unused")
public class CloneMethodWithGenericReturnWithExtends {

     /*
    todo: https://trello.com/c/jgLmn2pe/50-clone-clone-methods-with-generic-return-type
    @Clone(transformer = TestTransformer.class)
    <T extends Collection> T method(T param1, String param2) {
        return param1;
    }



    // ### Compilation - Test cases ###

    List<Integer> shouldBeAbleToUseGeneratedMethod() {
        return methodClone(new SomeType(), s());
    }

    private class SomeType { }
    */
}
