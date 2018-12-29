package kendal.experiments;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import kendal.annotations.Clone;
import kendal.api.inheritance.AttrReference;
import kendal.api.inheritance.Attribute;
import kendal.api.inheritance.Inherit;
import kendal.experiments.CloneTest.CsvTransformer;
import kendal.experiments.CloneTest.RequestMapping;

public class CloneNewExample {

    @Inherit(@Clone(transformer = CsvTransformer.class))
    @Attribute(name = "onMethod", value = {@RequestMapping(value = @AttrReference("endpoint"), method = "POST"), @Anno})
    @interface CsvEndpoint {
        String endpoint();
    }

    @CsvEndpoint(endpoint = "method1/csv")
    @RequestMapping(value = "/method1", method = "POST")
    public List<Object> method1() {
        return new ArrayList<>();
    }

    @CsvEndpoint(endpoint = "method2/csv")
    @RequestMapping(value = "/method2", method = "POST")
    public List<Object> method2() {
        return new ArrayList<>();
    }

    @CsvEndpoint(endpoint = "method3/csv")
    @RequestMapping(value = "/method3", method = "POST")
    public List<Object> method3() {
        return new ArrayList<>();
    }

    @Retention(value = RetentionPolicy.RUNTIME)
    @interface Anno {

    }
}
