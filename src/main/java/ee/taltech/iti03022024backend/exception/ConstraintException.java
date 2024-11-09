package ee.taltech.iti03022024backend.exception;

public class ConstraintException extends RuntimeException {
    public ConstraintException(String message) {
        super(message);
    }
}
