package kendal.api.exceptions;

public class InvalidArgumentException extends KendalRuntimeException {
    /**
     * @param message will be printed by {@link KendalProcessor} as a compilation error.
     */
    public InvalidArgumentException(String message) {
        super(message);
    }
}
