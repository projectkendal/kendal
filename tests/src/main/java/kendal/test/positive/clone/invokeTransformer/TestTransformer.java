package kendal.test.positive.clone.invokeTransformer;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

class TestTransformer implements Clone.Transformer<Integer, List<Integer>> {

    @Override
    public List<Integer> transform(Integer input) {
        ArrayList<Integer> result = new ArrayList<>();
        input++;
        result.add(input);
        return result;
    }
}
