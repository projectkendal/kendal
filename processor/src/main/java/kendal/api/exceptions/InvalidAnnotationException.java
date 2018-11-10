package kendal.api.exceptions;

import kendal.api.KendalHandler;
import kendal.processor.KendalProcessor;

/**
 * Exception thrown by {@link KendalHandler} to signal misuse of annotation.
 * Exception message is then printed out as a compilation error.
 *
 * Example: when annotation designed to be used only on constructor parameter is applied to method parameter.
 */
public class InvalidAnnotationException extends KendalException {

    /**
     *
     * @param message will be printed by {@link KendalProcessor} as a compilation error.
     */
    public InvalidAnnotationException(String message) {
        super(message);
    }
}
