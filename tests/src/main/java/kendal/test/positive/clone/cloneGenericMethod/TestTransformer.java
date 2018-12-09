package kendal.test.positive.clone.cloneGenericMethod;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

class TestTransformer<T> implements Clone.Transformer<T, List<Integer>> {

    @Override
    public List<Integer> transform(T input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input.toString().length() * 10);
        return result;
    }
}
