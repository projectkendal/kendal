package kendal.test.negative.clone;

import kendal.annotations.Clone;

import java.util.ArrayList;
import java.util.List;

class TestTransformer implements Clone.Transformer<Integer, List<Integer>> {

    @Override
    public List<Integer> transform(Integer input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input);
        return result;
    }
}
