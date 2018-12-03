package kendal.api.exceptions;

import kendal.api.KendalHandler;

/**
 * Exception thrown by {@link KendalHandler} in case of any errors. Exception message is then printed out
 * as a compilation error
 */
public class KendalRuntimeException extends RuntimeException {

    public KendalRuntimeException(String message) {
        super(message);
    }

}
