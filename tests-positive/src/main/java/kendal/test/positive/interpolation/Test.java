package kendal.test.positive.interpolation;

public class Test {
    private final String interpolatedField = +"whatever ${45}";


    private String interpolatedReturn() {
        return +"value ${interpolatedField}";
    }

    private void interpolatedMethodArg() {
        System.out.println(+"value ${interpolatedField}");
    }
}
