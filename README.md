# FluentValidator

A *fluent validation utility* for Java that provides a comprehensive, builder-style API to validate data with ease. Supports method chaining, lazy evaluation, and multiple error handling strategies.

---

## Key Features

- **Fluent Interface**: Chain multiple validations seamlessly.
- **Two Validation Modes**:
  - **Collect All Errors** — accumulates all validation failures before reporting.
  - **Fail Fast** — stops validation at the first error encountered.
- **Lazy Evaluation**: Uses `Supplier`s to defer computation until validation runs.
- **Type Safety**: Type-aware validation methods for various data types.
- **Comprehensive Coverage**: Null checks, emptiness, numeric ranges, string patterns, email validation, and custom conditions.
- **Customizable Error Handling**: Throw default or user-defined exceptions, or collect errors programmatically.

---

## Installation

Include the source in your project or add the compiled jar if published.

---

## Usage Examples

### Collect all errors

```java
FluentValidator.collectAll()
    .requireNonNull(() -> username, "Username")
    .requireNotBlank(() -> username, "Username")
    .requireValidEmail(() -> email, "Email")
    .requireInRange(() -> age, 18, 120, "Age")
    .validate();
````

### Fail fast

```java
FluentValidator.failFast()
    .requireNonNull(() -> user.getId(), "User ID")
    .requirePositiveOrZero(() -> user.getBalance(), "Account Balance")
    .validate();
```

### Custom error handling

```java
FluentValidator.collectAll()
    .requireNonEmpty(() -> userList, "User List")
    .requireThat(() -> userList.size(),
                 size -> size <= 100,
                 "User List Size",
                 "User list cannot contain more than 100 users")
    .validateOrFailWith(() -> new IllegalArgumentException("Invalid user data"));
```

### Collect errors without throwing

```java
List<String> errors = FluentValidator.collectAll()
    .requireNonNull(() -> data, "Data")
    .requireInRange(() -> value, 0, 100, "Value")
    .validateAndReturnErrors();

if (!errors.isEmpty()) {
    // Handle errors programmatically
    System.out.println("Validation errors: " + errors);
}
```

---

## Supported Validation Methods

| Category    | Methods                                                                                                               | Description                                                           |
| ----------- | --------------------------------------------------------------------------------------------------------------------- | --------------------------------------------------------------------- |
| Null Checks | `requireNonNull(Supplier, String)`                                                                                    | Validates values are not null                                         |
| Emptiness   | `requireNonEmpty(Supplier, String)`                                                                                   | Checks collections, arrays, strings, Optional, and maps for emptiness |
| String      | `requireNotBlank(Supplier, String)`, `requireMatches(Supplier, String, String)`                                       | Blank and regex pattern validations                                   |
| Numeric     | `requireNumeric(Supplier, String)`, `requireInRange(...)`, `requirePositiveOrZero(...)`, `requireNegativeOrZero(...)` | Numeric type and range checks                                         |
| Boolean     | `requireTrue(BooleanSupplier, String)`, `requireFalse(BooleanSupplier, String)`                                       | Boolean condition validations                                         |
| Email       | `requireValidEmail(Supplier, String)`                                                                                 | RFC-compliant email format validation                                 |
| Custom      | `requireThat(Supplier, Predicate, String, String)`                                                                    | Custom predicates with custom error messages                          |

---

## Validation Modes

* **Collect All Errors**
  Use `FluentValidator.collectAll()` to accumulate all errors before throwing a `ValidationException`.

* **Fail Fast**
  Use `FluentValidator.failFast()` to stop on the first validation failure for faster feedback.

---

## Exception Handling

* `validate()`
  Throws a `ValidationException` with detailed messages on failure.

* `validateOrFailWith(Supplier<? extends RuntimeException>)`
  Throws a user-supplied exception on validation failure.

* `validateAndReturnErrors()`
  Returns a list of error messages without throwing exceptions.

---

## Thread Safety

This class is **NOT thread-safe**. Create separate instances per thread or per validation sequence.

---

## Performance Notes

* Validation checks are lazy, executed only during validation.
* Fail-fast mode improves performance by short-circuiting.
* Regex patterns are compiled at runtime per check; consider caching if used extensively.

---

## License

---

## Author

Joseph Devereaux Libatique
