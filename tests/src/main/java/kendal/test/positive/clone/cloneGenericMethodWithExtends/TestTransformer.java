package kendal.test.positive.clone.cloneGenericMethodWithExtends;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kendal.annotations.Clone;

class TestTransformer<T extends Collection> implements Clone.Transformer<T, List<Integer>> {

    @Override
    public List<Integer> transform(T input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input.size());
        return result;
    }
}
