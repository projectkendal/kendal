package kendal.api.exceptions;

import kendal.api.KendalHandler;
import kendal.processor.KendalProcessor;

/**
 * Exception thrown by {@link KendalHandler} in case of any errors. Exception message is then printed out
 * as a compilation error
 */
public abstract class KendalException extends Exception {

    /**
     *
     * @param message will be printed by {@link KendalProcessor} as a compilation error.
     */
    KendalException(String message) {
        super(message);
    }
}
