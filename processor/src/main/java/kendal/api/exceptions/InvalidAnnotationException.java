package kendal.api.exceptions;

/**
 * Exception thrown by {@link kendal.api.KendalHandler} to signal misuse of annotation.
 *
 * Example: when annotation designed to be used only on constructor parameter is applied to method parameter.
 */

public class InvalidAnnotationException extends Exception {

    /**
     *
     * @param message will be printed by {@link kendal.processor.KendalProcessor} as compilation error.
     */
    public InvalidAnnotationException(String message) {
        super(message);
    }
}
