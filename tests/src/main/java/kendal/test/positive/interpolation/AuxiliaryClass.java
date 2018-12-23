package kendal.test.positive.interpolation;

@SuppressWarnings("unused")
class AuxiliaryClass {

    private String param1;
    private int param2;

    AuxiliaryClass() {}

    AuxiliaryClass(String param1, int param2) {
        this.param1 = param1;
        this.param2 = param2;
    }

    String getCreationResult() {
        return param1 + param2;
    }

    @Override
    public String toString() {
        return "aValueToBeExpected";
    }
}
