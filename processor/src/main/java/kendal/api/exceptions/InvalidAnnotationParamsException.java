package kendal.api.exceptions;

import kendal.api.KendalHandler;
import kendal.processor.KendalProcessor;

/**
 * Exception thrown by {@link KendalHandler} to signal invalid annotation parameters.
 * Exception message is then printed out as a compilation error.
 */
public class InvalidAnnotationParamsException extends KendalException {

    /**
     *
     * @param message will be printed by {@link KendalProcessor} as a compilation error.
     */
    public InvalidAnnotationParamsException(String message) {
        super(message);
    }
}
