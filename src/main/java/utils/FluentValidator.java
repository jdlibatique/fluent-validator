package utils;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;
import shared.exception.ValidationException;

/**
 * A fluent validation utility class that provides a comprehensive set of validation methods for
 * various data types and conditions. This class follows the builder pattern and supports method
 * chaining to create complex validation sequences with customizable error handling.
 *
 * <h2>Key Features</h2>
 * <ul>
 *   <li><strong>Fluent Interface:</strong> All validation methods return the FluentValidator instance,
 *       enabling method chaining for building complex validation sequences</li>
 *   <li><strong>Error Handling Strategies:</strong> Supports two validation modes:
 *       <ul>
 *         <li>Collect All Errors: Accumulates all validation errors before reporting</li>
 *         <li>Fail Fast: Stops validation at the first encountered error</li>
 *       </ul>
 *   </li>
 *   <li><strong>Lazy Evaluation:</strong> Uses Supplier functions for lazy evaluation of values,
 *       allowing for efficient validation of computed or expensive-to-calculate values</li>
 *   <li><strong>Type Safety:</strong> Provides type-safe validation methods for different data types</li>
 *   <li><strong>Comprehensive Coverage:</strong> Includes validators for null checks, emptiness,
 *       numeric ranges, string patterns, email validation, and custom conditions</li>
 * </ul>
 *
 * <h2>Usage Examples</h2>
 *
 * <h3>Basic Validation with Collect All Strategy</h3>
 * <pre>{@code
 * String username = "john_doe";
 * String email = "john@example.com";
 * Integer age = 25;
 *
 * FluentValidator.collectAll()
 *     .requireNonNull(() -> username, "Username")
 *     .requireNotBlank(() -> username, "Username")
 *     .requireValidEmail(() -> email, "Email")
 *     .requireInRange(() -> age, 18, 120, "Age")
 *     .validate();
 * }</pre>
 *
 * <h3>Fail Fast Validation</h3>
 * <pre>{@code
 * FluentValidator.failFast()
 *     .requireNonNull(() -> user.getId(), "User ID")
 *     .requirePositiveOrZero(() -> user.getBalance(), "Account Balance")
 *     .validate();
 * }</pre>
 *
 * <h3>Custom Error Handling</h3>
 * <pre>{@code
 * FluentValidator.collectAll()
 *     .requireNonEmpty(() -> userList, "User List")
 *     .requireThat(() -> userList.size(),
 *                  size -> size <= 100,
 *                  "User List Size",
 *                  "User list cannot contain more than 100 users")
 *     .validateOrFailWith(() -> new IllegalArgumentException("Invalid user data"));
 * }</pre>
 *
 * <h3>Collecting Errors Without Throwing Exceptions</h3>
 * <pre>{@code
 * List<String> errors = FluentValidator.collectAll()
 *     .requireNonNull(() -> data, "Data")
 *     .requireInRange(() -> value, 0, 100, "Value")
 *     .validateAndReturnErrors();
 *
 * if (!errors.isEmpty()) {
 *     // Handle errors programmatically
 *     log.warn("Validation errors: {}", errors);
 * }
 * }</pre>
 *
 * <h2>Validation Methods Overview</h2>
 * <table border="1">
 *   <tr>
 *     <th>Category</th>
 *     <th>Methods</th>
 *     <th>Description</th>
 *   </tr>
 *   <tr>
 *     <td>Null Checks</td>
 *     <td>{@link #requireNonNull(Supplier, String)}</td>
 *     <td>Validates that values are not null</td>
 *   </tr>
 *   <tr>
 *     <td>Emptiness</td>
 *     <td>{@link #requireNonEmpty(Supplier, String)}</td>
 *     <td>Validates Collections, Maps, Arrays, Optional, and CharSequence are not empty</td>
 *   </tr>
 *   <tr>
 *     <td>String Validation</td>
 *     <td>{@link #requireNotBlank(Supplier, String)}, {@link #requireMatches(Supplier, String, String)}</td>
 *     <td>String-specific validations including regex pattern matching</td>
 *   </tr>
 *   <tr>
 *     <td>Numeric Validation</td>
 *     <td>{@link #requireNumeric(Supplier, String)}, {@link #requireInRange(Supplier, double, double, String)},
 *         {@link #requirePositiveOrZero(Supplier, String)}, {@link #requireNegativeOrZero(Supplier, String)}</td>
 *     <td>Numeric type and range validations</td>
 *   </tr>
 *   <tr>
 *     <td>Boolean Conditions</td>
 *     <td>{@link #requireTrue(BooleanSupplier, String)}, {@link #requireFalse(BooleanSupplier, String)}</td>
 *     <td>Boolean condition validation</td>
 *   </tr>
 *   <tr>
 *     <td>Specialized</td>
 *     <td>{@link #requireValidEmail(Supplier, String)}</td>
 *     <td>Email format validation with RFC compliance</td>
 *   </tr>
 *   <tr>
 *     <td>Custom</td>
 *     <td>{@link #requireThat(Supplier, Predicate, String, String)}</td>
 *     <td>Custom validation logic with predicates</td>
 *   </tr>
 * </table>
 *
 * <h2>Error Handling Strategies</h2>
 *
 * <h3>Collect All Errors Strategy</h3>
 * <p>Created using {@link #collectAll()}, this strategy accumulates all validation errors
 * before throwing an exception. This is useful when you want to provide comprehensive
 * feedback to users about all validation issues at once.</p>
 *
 * <h3>Fail Fast Strategy</h3>
 * <p>Created using {@link #failFast()}, this strategy stops validation at the first
 * encountered error. This is more efficient when you only need to know if validation
 * passes or fails, without requiring details about all possible errors.</p>
 *
 * <h2>Supported Data Types</h2>
 * <ul>
 *   <li><strong>Numeric Types:</strong> All Java numeric types including BigDecimal,
 *       both primitive and wrapper classes (byte, short, int, long, float, double)</li>
 *   <li><strong>Collections:</strong> List, Set, Map, and other Collection implementations</li>
 *   <li><strong>Arrays:</strong> All array types including primitive arrays</li>
 *   <li><strong>Text:</strong> String, StringBuilder, StringBuffer, and other CharSequence implementations</li>
 *   <li><strong>Optional:</strong> Java Optional type</li>
 *   <li><strong>Custom Objects:</strong> Through the requireThat method with custom predicates</li>
 * </ul>
 *
 * <h2>Exception Handling</h2>
 * <p>The class provides three methods for handling validation results:</p>
 * <ul>
 *   <li>{@link #validate()} - Throws {@link ValidationException} with detailed error information</li>
 *   <li>{@link #validateOrFailWith(Supplier)} - Throws a custom exception provided by the supplier</li>
 *   <li>{@link #validateAndReturnErrors()} - Returns a list of error messages without throwing exceptions</li>
 * </ul>
 *
 * <h2>Thread Safety</h2>
 * <p>This class is <strong>NOT thread-safe</strong>. Each validation sequence should be performed
 * within a single thread. If validation is needed across multiple threads, create separate
 * FluentValidator instances for each thread.</p>
 *
 * <h2>Performance Considerations</h2>
 * <ul>
 *   <li>Suppliers are evaluated only when validation is performed, enabling lazy evaluation</li>
 *   <li>Fail-fast strategy provides better performance when only the first error is needed</li>
 *   <li>String regex compilation is performed for each validation; consider caching compiled patterns
 *       for frequently used regex patterns</li>
 * </ul>
 *
 * <h2>Email Validation Details</h2>
 * <p>The {@link #requireValidEmail(Supplier, String)} method validates email addresses using:</p>
 * <ul>
 *   <li>A regex pattern that follows basic email format rules</li>
 *   <li>Maximum length validation (254 characters as per RFC specifications)</li>
 *   <li>Basic format validation including domain and top-level domain requirements</li>
 * </ul>
 *
 * @author Joseph Devereaux Libatique
 * @version 1.0-SNAPSHOT
 * @see ValidationException
 * @see Supplier
 * @see Predicate
 * @see BooleanSupplier
 * @since 1.0-SNAPSHOT
 */

public final class FluentValidator {

    private static final String EMAIL_REGEX = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
    private static final int MAX_EMAIL_LENGTH = 254;

    private final List<Supplier<String>> errorChecks = new LinkedList<>();
    private final boolean failFast;

    private FluentValidator(boolean failFast) {
        this.failFast = failFast;
    }

    /**
     * Creates a FluentValidator instance that collects all validation errors.
     *
     * @return a FluentValidator instance
     */
    public static FluentValidator collectAll() {
        return new FluentValidator(false);
    }

    /**
     * Creates a FluentValidator instance that stops at the first validation error.
     *
     * @return a FluentValidator instance
     */
    public static FluentValidator failFast() {
        return new FluentValidator(true);
    }


    /**
     * Ensures that the supplied value is not null. If the value is null, a validation error message
     * is registered. This method helps validate input and track nullable fields as part of the
     * validation process.
     *
     * @param valueSupplier the supplier providing the value to be validated for nullity
     * @param name the name or identifier of the value being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireNonNull(Supplier<?> valueSupplier, String name) {
        String errorMessage = "%s must not be null".formatted(name);

        errorChecks.add(() -> {
            if (valueSupplier.get() == null) {
                return errorMessage;
            }

            return null;
        });

        return this;
    }

    /**
     * Ensures that the supplied value is not empty. This method checks for emptiness in various
     * types such as Optional, CharSequence, arrays, Collections, and Maps. If the value is empty, a
     * validation error message is registered.
     *
     * @param valueSupplier the supplier providing the value to be validated for emptiness
     * @param name the name or identifier of the value being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireNonEmpty(Supplier<?> valueSupplier, String name) {
        requireNonNull(valueSupplier, name);

        String errorMessage = "%s must not be empty".formatted(name);

        errorChecks.add(() -> {
            Object value = valueSupplier.get();

            if (value instanceof Optional<?> optional && optional.isEmpty()) {
                return errorMessage;
            }

            if (value instanceof CharSequence charSequence && charSequence.isEmpty()) {
                return errorMessage;
            }

            if (value.getClass().isArray() && Array.getLength(value) == 0) {
                return errorMessage;
            }

            if (value instanceof Collection<?> collection && collection.isEmpty()) {
                return errorMessage;
            }

            if (value instanceof Map<?, ?> map && map.isEmpty()) {
                return errorMessage;
            }

            return null;
        });

        return this;
    }

    /**
     * Ensures that the supplied CharSequence value is not blank (i.e., not null, empty, or only
     * whitespace). If the value is blank, a validation error message is registered.
     *
     * @param valueSupplier the supplier providing the CharSequence value to be validated
     * @param name the name or identifier of the value being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireNotBlank(Supplier<CharSequence> valueSupplier, String name) {
        requireNonNull(valueSupplier, name);

        String errorMessage = "%s must not be blank".formatted(name);

        errorChecks.add(() -> {
            CharSequence value = valueSupplier.get();

            if (value == null || value.toString().trim().isEmpty()) {
                return errorMessage;
            }

            return null;
        });

        return this;
    }

    /**
     * Ensures that the supplied value is numeric. This method checks if the value is an instance of
     * Number (e.g., Integer, Long, Double, etc.). If the value is not numeric, a validation error
     * message is registered.
     *
     * @param valueSupplier the supplier providing the value to be validated for numeric type
     * @param name the name or identifier of the value being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireNumeric(Supplier<?> valueSupplier, String name) {
        requireNonNull(valueSupplier, name);

        String errorMessage = "%s must be numeric".formatted(name);

        errorChecks.add(() -> {
            Object value = valueSupplier.get();

            if (!(value instanceof Number)) {
                return errorMessage;
            }

            return null;
        });

        return this;
    }

    /**
     * Ensures that the supplied numeric value is within a specified range (inclusive). If the value
     * is outside the range, a validation error message is registered.
     *
     * @param valueSupplier the supplier providing the numeric value to be validated
     * @param min the minimum acceptable value (inclusive)
     * @param max the maximum acceptable value (inclusive)
     * @param name the name or identifier of the value being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireInRange(
        Supplier<?> valueSupplier,
        double min,
        double max,
        String name
    ) {
        requireNonNull(valueSupplier, name);
        requireNumeric(valueSupplier, name);

        String rangeExceededErrorMessage = "%s must be between %s and %s".formatted(name, min, max);

        errorChecks.add(() -> {
            return switch (valueSupplier.get()) {
                case BigDecimal bigDecimalValue when
                    bigDecimalValue.compareTo(BigDecimal.valueOf(min)) < 0 ||
                        bigDecimalValue.compareTo(BigDecimal.valueOf(max)) > 0 ->
                    rangeExceededErrorMessage;
                case Byte byteValue when byteValue < min || byteValue > max ->
                    rangeExceededErrorMessage;
                case Short shortValue when shortValue < min || shortValue > max ->
                    rangeExceededErrorMessage;
                case Integer integerValue when integerValue < min || integerValue > max ->
                    rangeExceededErrorMessage;
                case Long longValue when longValue < min || longValue > max ->
                    rangeExceededErrorMessage;
                case Float floatValue when floatValue < min || floatValue > max ->
                    rangeExceededErrorMessage;
                case Double doubleValue when doubleValue < min || doubleValue > max ->
                    rangeExceededErrorMessage;
                default -> null;
            };
        });

        return this;
    }

    /**
     * Ensures that the supplied numeric value is positive (greater than zero). If the value is not
     * positive, a validation error message is registered.
     *
     * @param valueSupplier the supplier providing the numeric value to be validated
     * @param name the name or identifier of the value being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requirePositiveOrZero(Supplier<?> valueSupplier, String name) {
        requireNonNull(valueSupplier, name);
        requireNumeric(valueSupplier, name);

        String errorMessage = "%s must be positive or zero".formatted(name);

        errorChecks.add(() -> {
            return switch (valueSupplier.get()) {
                case BigDecimal bigDecimalValue when
                    bigDecimalValue.compareTo(BigDecimal.ZERO) < 0 -> errorMessage;
                case Byte byteValue when byteValue < 0 -> errorMessage;
                case Short shortValue when shortValue < 0 -> errorMessage;
                case Integer integerValue when integerValue < 0 -> errorMessage;
                case Long longValue when longValue < 0 -> errorMessage;
                case Float floatValue when floatValue < 0 -> errorMessage;
                case Double doubleValue when doubleValue < 0 -> errorMessage;
                default -> null;
            };
        });

        return this;
    }

    /**
     * Ensures that the supplied numeric value is negative (less than zero). If the value is not
     * negative, a validation error message is registered.
     *
     * @param valueSupplier the supplier providing the numeric value to be validated
     * @param name the name or identifier of the value being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireNegativeOrZero(Supplier<?> valueSupplier, String name) {
        requireNonNull(valueSupplier, name);
        requireNumeric(valueSupplier, name);

        String errorMessage = "%s must be negative or zero".formatted(name);

        errorChecks.add(() -> {
            return switch (valueSupplier.get()) {
                case BigDecimal bigDecimalValue when
                    bigDecimalValue.compareTo(BigDecimal.ZERO) > 0 -> errorMessage;
                case Byte byteValue when byteValue > 0 -> errorMessage;
                case Short shortValue when shortValue > 0 -> errorMessage;
                case Integer integerValue when integerValue > 0 -> errorMessage;
                case Long longValue when longValue > 0 -> errorMessage;
                case Float floatValue when floatValue > 0 -> errorMessage;
                case Double doubleValue when doubleValue > 0 -> errorMessage;
                default -> null;
            };
        });

        return this;
    }

    /**
     * Ensures that the supplied CharSequence value matches a specified regular expression. If the
     * value does not match, a validation error message is registered.
     *
     * @param valueSupplier the supplier providing the CharSequence value to be validated
     * @param regex the regular expression that the value must match
     * @param name the name or identifier of the value being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireMatches(
        Supplier<CharSequence> valueSupplier,
        String regex,
        String name
    ) {
        requireNonNull(valueSupplier, name);
        requireNotBlank(valueSupplier, name);

        String errorMessage = "%s must match pattern: %s".formatted(name, regex);

        errorChecks.add(() -> {
            CharSequence value = valueSupplier.get();
            if (!value.toString().matches(regex)) {
                return errorMessage;
            }
            return null;
        });

        return this;
    }

    /**
     * Ensures that the supplied condition is true. If the condition is false, a validation error
     * message is registered.
     *
     * @param conditionSupplier a supplier that provides the boolean condition to be validated
     * @param name the name or identifier of the condition being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireTrue(BooleanSupplier conditionSupplier, String name) {
        String errorMessage = "%s must be true".formatted(name);
        errorChecks.add(() -> conditionSupplier.getAsBoolean() ? null : errorMessage);

        return this;
    }

    /**
     * Ensures that the supplied condition is false. If the condition is true, a validation error
     * message is registered.
     *
     * @param conditionSupplier a supplier that provides the boolean condition to be validated
     * @param name the name or identifier of the condition being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireFalse(BooleanSupplier conditionSupplier, String name) {
        String errorMessage = "%s must be false".formatted(name);
        errorChecks.add(() -> !conditionSupplier.getAsBoolean() ? null : errorMessage);

        return this;
    }

    /**
     * Ensures that the supplied CharSequence value is a valid email address. If the value is not a
     * valid email, a validation error message is registered.
     *
     * @param emailSupplier the supplier providing the CharSequence value to be validated as an
     * email
     * @param name the name or identifier of the email being validated, used in error messages
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public FluentValidator requireValidEmail(Supplier<CharSequence> emailSupplier, String name) {
        requireNonNull(emailSupplier, name);
        requireNotBlank(emailSupplier, name);

        String errorMessage = "%s must be a valid email address".formatted(name);

        errorChecks.add(() -> {
            String email = emailSupplier.get().toString();

            if (!email.matches(EMAIL_REGEX) || email.length() > MAX_EMAIL_LENGTH) {
                return errorMessage;
            }

            return null;
        });

        return this;
    }

    /**
     * Ensures that the supplied value satisfies a specified condition. If the condition is not met,
     * a validation error message is registered.
     *
     * @param valueSupplier the supplier providing the value to be validated
     * @param condition the predicate that defines the condition to be satisfied
     * @param name the name or identifier of the value being validated, used in error messages
     * @param message the error message to be registered if the condition is not met
     * @return the current instance of {@code FluentValidator} to allow method chaining
     */
    public <T> FluentValidator requireThat(
        Supplier<T> valueSupplier,
        Predicate<T> condition,
        String name,
        String message
    ) {
        requireNonNull(valueSupplier, name);

        errorChecks.add(() -> {
            T value = valueSupplier.get();

            if (!condition.test(value)) {
                return message;
            }

            return null;
        });

        return this;
    }

    /**
     * Validates all registered error checks. If any error check fails, a ValidationException is
     * thrown. The exception contains a message and a list of all validation errors.
     *
     * If failFast is true, validation stops at the first error encountered.
     */
    public void validate() {
        if (failFast) {
            for (Supplier<String> errorCheck : errorChecks) {
                String error = errorCheck.get();
                if (error != null) {
                    throw new ValidationException("Validation failed: %s".formatted(error),
                        List.of(error)
                    );
                }
            }

            return;
        }

        List<String> errors = errorChecks.stream()
            .filter(error -> error.get() != null)
            .map(Supplier::get)
            .toList();

        if (!errors.isEmpty()) {
            throw new ValidationException(
                "Validation failed: %s".formatted(String.join(", ", errors)),
                errors
            );
        }
    }

    /**
     * Validates all registered error checks. If any error check fails, a RuntimeException is
     * thrown. The exception is provided by the supplied exceptionSupplier.
     *
     * If failFast is true, validation stops at the first error encountered.
     *
     * @param exceptionSupplier a supplier that provides the RuntimeException to be thrown on
     * validation failure
     */
    public void validateOrFailWith(Supplier<? extends RuntimeException> exceptionSupplier) {
        if (failFast) {
            for (Supplier<String> errorCheck : errorChecks) {
                String error = errorCheck.get();
                if (error != null) {
                    throw exceptionSupplier.get();
                }
            }

            return;
        }

        List<String> errors = errorChecks.stream()
            .filter(error -> error.get() != null)
            .map(Supplier::get)
            .toList();

        if (!errors.isEmpty()) {
            throw exceptionSupplier.get();
        }
    }

    /**
     * Validates all registered error checks and returns a list of error messages. If any error
     * check fails, the corresponding error message is included in the list. This method does not
     * throw an exception; it simply collects and returns the errors.
     *
     * @return a list of validation error messages
     */
    public List<String> validateAndReturnErrors() {
        return errorChecks.stream()
            .map(Supplier::get)
            .filter(error -> error != null)
            .toList();
    }
}
