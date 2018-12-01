package kendal.api.exceptions;

import kendal.api.KendalHandler;
import kendal.processor.KendalProcessor;

/**
 * Exception thrown by {@link KendalHandler} when an element (field/method/etc...) with given
 * configuration (name/parameters/etc...) that is to be generated, already exists in the destination class.
 */
public class DuplicatedElementsException extends KendalException {

    /**
     * @param message
     *         will be printed by {@link KendalProcessor} as a compilation error.
     */
    public DuplicatedElementsException(String message) {
        super(message);
    }
}
