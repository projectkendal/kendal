package kendal.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

public class KendalMessager {

    private final Messager messager;

    public KendalMessager(Messager messager) {
        this.messager = messager;
    }

    public void printMessage(Diagnostic.Kind kind, String message) {
        messager.printMessage(kind, "[Kendal] " + message);
    }

    public void printElapsedTime(String what, long since) {
        long elapsedTime = System.currentTimeMillis() - since;
        printMessage(Diagnostic.Kind.NOTE, what + " execution time: " + elapsedTime + " milliseconds");
    }

}
