package kendal.experiments.subpackage;

import kendal.annotations.Clone;

public class TestClassTransformer implements Clone.Transformer<Object, String> {

    @Override
    public String transform(Object input) {
        return "asdas";
    }
}