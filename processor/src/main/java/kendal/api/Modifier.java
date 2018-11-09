package kendal.api;

import com.sun.tools.javac.code.Flags;

public enum Modifier {
    PACKAGE_PRIVATE(0),
    PRIVATE(Flags.PRIVATE),
    PROTECTED(Flags.PROTECTED),
    PUBLIC(Flags.PUBLIC);

    private int flag;

    Modifier(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return flag;
    }
}
