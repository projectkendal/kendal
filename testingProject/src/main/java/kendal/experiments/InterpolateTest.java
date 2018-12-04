package kendal.experiments;

public class InterpolateTest {

    private int field = 17;
    private Object o = null;

    public String test() {
        return +"field equals ${field} and wunderbar ${field} $$object=${o}";
    }
}
