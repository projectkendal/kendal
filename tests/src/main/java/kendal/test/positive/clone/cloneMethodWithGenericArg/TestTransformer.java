package kendal.test.positive.clone.cloneMethodWithGenericArg;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

class TestTransformer implements Clone.Transformer<Integer, List<Integer>> {

    @Override
    public List<Integer> transform(Integer input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input.toString().length() * 10);
        return result;
    }
}
