package kendal.experiments.subpackage;

import java.io.Serializable;

import kendal.annotations.Clone;

public class TestClassTransformer extends Subclass implements Serializable, Clone.Transformer<String, Integer> {
    @Override
    public int transform(long param) {
        return (int) param;
    }

    @Override
    public Integer transform(String input) {
        return 15;
    }
}