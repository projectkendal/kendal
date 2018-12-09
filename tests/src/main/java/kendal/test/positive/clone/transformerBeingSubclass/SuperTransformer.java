package kendal.test.positive.clone.transformerBeingSubclass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

class SuperTransformer implements Serializable, Clone.Transformer<Integer, List<Integer>> {

    @Override
    public List<Integer> transform(Integer input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input);
        return result;
    }
}
