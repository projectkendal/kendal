package kendal.test.positive.clone.cloneMethodThrowingException;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

public class TestTransformer implements Clone.Transformer<Integer, List<Integer>> {

    @Override
    public List<Integer> transform(Integer input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input);
        return result;
    }
}
