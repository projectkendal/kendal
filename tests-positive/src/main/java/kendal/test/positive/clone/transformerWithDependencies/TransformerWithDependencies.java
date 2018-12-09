package kendal.test.positive.clone.transformerWithDependencies;

import static kendal.test.positive.utils.ValuesGenerator.s;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

class TransformerWithDependencies extends AbstractClassWithTransformMethod
        implements Serializable, Clone.Transformer<Integer, List<Integer>> {

    @Override
    String transform(Boolean param) {
        return s();
    }

    @Override
    public List<Integer> transform(Integer input) {
        ArrayList<Integer> result = new ArrayList<>();
        result.add(input);
        return result;
    }
}
