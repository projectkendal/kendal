package kendal.test.positive.clone.cloneMethodWithGenericReturn;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

public class TestTransformer<T> implements Clone.Transformer<T, List<Integer>> {

    @Override
    public List<Integer> transform(T input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input.toString().length());
        return result;
    }
}
