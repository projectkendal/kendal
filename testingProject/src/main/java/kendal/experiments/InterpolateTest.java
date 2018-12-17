package kendal.experiments;

public class InterpolateTest {

    private int field = 17;
    private Object o = null;

    private static final String nameOfVariableBelow = "interpolated string";
    protected String interpolated = +"Ich bin ein ${nameOfVariableBelow}";

    public static void main(String[] args) {
        System.out.println(+"field + 29 = ${new InterpolateTest().field + 29}");
        System.out.println(+"someNumber + 723 = ${someNumber() + 723}");
    }

    private static String meth(Object lol) {
        return lol.toString();
    }

    private static int someNumber() {
        return 101;
    }


    public String test() {
        return +"field equals ${field} and wunderbar ${field} $$object=${o}";
    }

    public void test2() {
        String first = "some constant string";
        String uberInterpolated = first + +"Using other interpolated string ${interpolated} in contatenations with ${first}";

        methodWithStringArg(+"Let's just see what happens with other kind of expression ${field + 29 + 16 + uberInterpolated}");
    }

    private boolean methodWithStringArg(String arg) {
        arg.compareToIgnoreCase(+"It will not be equal but whatever... ${interpolated}");
        meth(+"It will not be equal but whatever... ${interpolated}");
        return arg.equals(+"It will not be equal but whatever... ${interpolated}");
    }
}
