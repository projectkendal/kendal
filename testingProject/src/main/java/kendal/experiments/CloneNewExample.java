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

    @Inherit(@Clone(transformer = CsvTransformer.class, methodName = "clonedMethod1"))
    @Attribute(name = "onMethod", value = {@RequestMapping(value = @AttrReference("endpoint"), method = "POST"), @Anno})
    @interface CsvEndpoint {
        String endpoint();
    }

    @CsvEndpoint(endpoint = "method1/csv")
    @RequestMapping(value = "/method1", method = "POST")
    public List<Object> method1() {
        return new ArrayList<>();
    }

    String cloneUserNotWorking() {
        return clonedMethod1();
    }

    @Clone(transformer = CsvTransformer.class, methodName = "clonedMethod2")
    @RequestMapping(value = "/method2", method = "POST")
    public List<Object> method2() {
        return new ArrayList<>();
    }

    String cloneUserWorking() {
        return clonedMethod2();
    }

    @Clone(transformer = CsvTransformer.class)
    @RequestMapping(value = "/method3", method = "POST")
    public List<Object> method3() {
        return new ArrayList<>();
    }

    String cloneUserWorking2() {
        return method3Clone();
    }

    @Retention(value = RetentionPolicy.RUNTIME)
    @interface Anno {

    }
}
