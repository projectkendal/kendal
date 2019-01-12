package kendal.test.positive.clone.cloneMethodWithGenericReturnWithExtends;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

public class TestTransformer<T extends SuperType> implements Clone.Transformer<T, List<Integer>> {

    @Override
    public List<Integer> transform(T input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input.toString().length());
        return result;
    }
}