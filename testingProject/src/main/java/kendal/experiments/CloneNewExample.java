package kendal.experiments;

import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;
import kendal.api.inheritance.AttrReference;
import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;
import kendal.experiments.CloneTest.RequestMapping;

public class CloneNewExample {

    @Inherit(@Clone(transformer = CsvTransformer.class))
    @Attribute(name = "onMethod", value = {@RequestMapping(value = @AttrReference("endpoint"), method = "POST"),
            @AnotherAnnotation})
    @interface CsvEndpoint {
        String endpoint();
    }

    class CsvTransformer implements Clone.Transformer<List<Object>, String> {
        @Override
        public String transform(List<Object> inputCollection) {
            return "imagine here is the original collection serialized to csv";
        }
    }

    @CsvEndpoint(endpoint = "method1/csv", methodName = "method1Csv")
    @RequestMapping(value = "/method1", method = "POST")
    public List<Object> method1(Object body) {
        List<Object> result = new ArrayList();
        // some logic
        return result;
    }

    @CsvEndpoint(endpoint = "method2/csv", methodName = "method2Csv")
    @RequestMapping(value = "/method2", method = "POST")
    public List<Object> method2(Object body) {
        List<Object> result = new ArrayList();
        // some logic
        method1Csv(new ArrayList<>());
        return result;
    }

    @interface AnotherAnnotation {}
}