package shared.exception;

import java.io.Serial;
import java.util.List;

/**
 * A custom runtime exception used to handle validation errors that occur during
 * data processing or business logic validation.
 *
 * <p>This exception extends {@link RuntimeException} and provides additional
 * functionality to store and retrieve multiple validation error messages.
 * It is typically thrown when input data fails validation checks and contains
 * a list of specific error messages describing what went wrong.</p>
 *
 * <p>Example usage:</p>
 * <pre>{@code
 * List<String> errors = Arrays.asList(
 *     "Name cannot be empty",
 *     "Email format is invalid",
 *     "Age must be between 18 and 65"
 * );
 * throw new ValidationException("Validation failed for user input", errors);
 * }</pre>
 *
 * @author Joseph Devereaux Libatique
 * @version 1.0-SNAPSHOT
 * @since 1.0-SNAPSHOT
 */

public class ValidationException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 7999570545538959647L;

    private final List<String> errors;

    public ValidationException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public List<String> getErrors() {
        return errors;
    }
}
