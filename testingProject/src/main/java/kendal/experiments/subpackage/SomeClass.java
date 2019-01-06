package kendal.experiments.subpackage;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;

class SomeClass {

    class CsvTransformer implements Clone.Transformer<List<Object>, String> {
        @Override
        public String transform(List<Object> inputCollection) {
            return "imagine here is the original collection serialized to csv";
        }
    }

    @Clone(transformer = CsvTransformer.class, methodName="method1Csv")
    List<Object> method1(int oneParam, String anotherParam) {
        List<Object> result = new ArrayList();
        // some logic
        return result;
    }

    @Clone(transformer = CsvTransformer.class, methodName="method2Csv")
    List<Object> method2(int oneParam, String anotherParam) {
        List<Object> result = new ArrayList();
        // some logic
        return result;
    }

}
