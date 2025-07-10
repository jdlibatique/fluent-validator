package io.github.jdlibatique.fluentvalidator.core;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.assertThatNoException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import io.github.jdlibatique.fluentvalidator.exception.ValidationException;

/*
 * Copyright 2025 Joseph Devereaux Libatique
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Joseph Devereaux Libatique
 */
class FluentValidatorTest {

    @Test
    void testValidate_WhenAllValidationsPass_ShouldNotThrowException() {
        String username = "testUser";

        assertThatNoException().isThrownBy(() -> {
            FluentValidator.collectAll()
                .requireNonNull(() -> username, "Username")
                .requireNotBlank(() -> username, "Username")
                .validate();
        });
    }

    @Test
    void testValidateOrFailWith_WhenAllValidationsPass_ShouldNotThrowException() {
        String username = "testUser";

        assertThatNoException().isThrownBy(() -> {
            FluentValidator.collectAll()
                .requireNonNull(() -> "testUser", "Username")
                .requireNotBlank(() -> "testUser", "Username")
                .validateOrFailWith(() -> new IllegalArgumentException("Validation passed"));
        });
    }

    @Test
    void testValidateAndReturnErrors_WhenAllValidationsPass_ShouldReturnEmptyListAndNotThrowException() {
        String username = "testUser";

        assertThatNoException().isThrownBy(() -> {
            List<String> errors = FluentValidator.collectAll()
                .requireNonNull(() -> username, "Username")
                .requireNotBlank(() -> username, "Username")
                .validateAndReturnErrors();
            assertThat(errors).isEmpty();
        });
    }

    @Test
    void testValidate_WhenValidationFails_ShouldThrowValidationException() {
        String nullString = null;
        ArrayList<String> emptyArray = new ArrayList<>();
        String blankString = "";
        String nonNumericString = "not a number";
        Integer notInRangeInteger = 200;
        Integer negativeInteger = -5;
        Integer postiveInteger = 10;
        String notMatchingPatternString = "invalid-email";
        boolean falseValue = false;
        boolean trueValue = true;
        String invalidEmail = "invalid-email";
        Integer notRequiredInteger = 10;

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonNull(() -> nullString, "Null String")
                .requireNonEmpty(() -> emptyArray, "Empty Array")
                .requireNotBlank(() -> blankString, "Blank String")
                .requireNumeric(() -> nonNumericString, "Non-Numeric String")
                .requireInRange(() -> notInRangeInteger, 0, 100, "Not In Range Integer")
                .requirePositiveOrZero(() -> negativeInteger, "Negative Integer")
                .requireNegativeOrZero(() -> postiveInteger, "Positive Integer")
                .requireMatches(() -> notMatchingPatternString,
                    "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$", "Not Matching Pattern String"
                )
                .requireTrue(() -> falseValue, "False Value")
                .requireFalse(() -> trueValue, "True Value")
                .requireValidEmail(() -> invalidEmail, "Invalid Email")
                .requireInRange(() -> notRequiredInteger, 0, 100, "Not Required Integer")
                .requireThat(() ->
                        notRequiredInteger,
                    r -> r == 0, "Not Required Integer",
                    "Not Required Integer must be equal to zero"
                )
                .validate())
            .actual();

        assertThat(exception).hasMessage("Validation failed: "
            + "Null String must not be null, "
            + "Empty Array must not be empty, "
            + "Blank String must not be blank, "
            + "Non-Numeric String must be numeric, "
            + "Not In Range Integer must be between 0.0 and 100.0, "
            + "Negative Integer must be positive or zero, "
            + "Positive Integer must be negative or zero, "
            + "Not Matching Pattern String must match pattern: ^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$, "
            + "False Value must be true, "
            + "True Value must be false, "
            + "Invalid Email must be a valid email address, "
            + "Not Required Integer must be equal to zero"
        );

        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).hasSize(12);

        assertThat(exception.getErrors()).contains(
            "Null String must not be null",
            "Empty Array must not be empty",
            "Blank String must not be blank",
            "Non-Numeric String must be numeric",
            "Not In Range Integer must be between 0.0 and 100.0",
            "Negative Integer must be positive or zero",
            "Positive Integer must be negative or zero",
            "Not Matching Pattern String must match pattern: ^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$",
            "False Value must be true",
            "True Value must be false",
            "Invalid Email must be a valid email address",
            "Not Required Integer must be equal to zero"
        );
    }

    @Test
    void testValidateOrFailWith_WhenValidationFails_ShouldThrowValidationException() {
        String nullString = null;
        ArrayList<String> emptyArray = new ArrayList<>();
        String blankString = "";
        String nonNumericString = "not a number";
        Integer notInRangeInteger = 200;
        Integer negativeInteger = -5;
        Integer postiveInteger = 10;
        String notMatchingPatternString = "invalid-email";
        boolean falseValue = false;
        boolean trueValue = true;
        String invalidEmail = "invalid-email";
        Integer notRequiredInteger = 10;

        IllegalArgumentException exception = assertThatExceptionOfType(
            IllegalArgumentException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonNull(() -> nullString, "Null String")
                .requireNonEmpty(() -> emptyArray, "Empty Array")
                .requireNotBlank(() -> blankString, "Blank String")
                .requireNumeric(() -> nonNumericString, "Non-Numeric String")
                .requireInRange(() -> notInRangeInteger, 0, 100, "Not In Range Integer")
                .requirePositiveOrZero(() -> negativeInteger, "Negative Integer")
                .requireNegativeOrZero(() -> postiveInteger, "Positive Integer")
                .requireMatches(() -> notMatchingPatternString,
                    "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$", "Not Matching Pattern String"
                )
                .requireTrue(() -> falseValue, "False Value")
                .requireFalse(() -> trueValue, "True Value")
                .requireValidEmail(() -> invalidEmail, "Invalid Email")
                .requireInRange(() -> notRequiredInteger, 0, 100, "Not Required Integer")
                .requireThat(() ->
                        notRequiredInteger,
                    r -> r == 0, "Not Required Integer",
                    "Not Required Integer must be equal to zero"
                )
                .validateOrFailWith(() -> new IllegalArgumentException("Validation failed")))
            .actual();

        assertThat(exception).hasMessage("Validation failed");
    }

    @Test
    void testValidateAndReturnErrors_WhenValidationFails_ShouldReturnListAndNotThrowException() {
        String nullString = null;
        ArrayList<String> emptyArray = new ArrayList<>();
        String blankString = "";
        String nonNumericString = "not a number";
        Integer notInRangeInteger = 200;
        Integer negativeInteger = -5;
        Integer postiveInteger = 10;
        String notMatchingPatternString = "invalid-email";
        boolean falseValue = false;
        boolean trueValue = true;
        String invalidEmail = "invalid-email";
        Integer notRequiredInteger = 10;

        assertThatNoException()
            .isThrownBy(() -> {
                List<String> errors = FluentValidator.collectAll()
                    .requireNonNull(() -> nullString, "Null String")
                    .requireNonEmpty(() -> emptyArray, "Empty Array")
                    .requireNotBlank(() -> blankString, "Blank String")
                    .requireNumeric(() -> nonNumericString, "Non-Numeric String")
                    .requireInRange(() -> notInRangeInteger, 0, 100, "Not In Range Integer")
                    .requirePositiveOrZero(() -> negativeInteger, "Negative Integer")
                    .requireNegativeOrZero(() -> postiveInteger, "Positive Integer")
                    .requireMatches(() -> notMatchingPatternString,
                        "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$", "Not Matching Pattern String"
                    )
                    .requireTrue(() -> falseValue, "False Value")
                    .requireFalse(() -> trueValue, "True Value")
                    .requireValidEmail(() -> invalidEmail, "Invalid Email")
                    .requireInRange(() -> notRequiredInteger, 0, 100, "Not Required Integer")
                    .requireThat(() ->
                            notRequiredInteger,
                        r -> r == 0, "Not Required Integer",
                        "Not Required Integer must be equal to zero"
                    )
                    .validateAndReturnErrors();

                assertThat(errors).isNotEmpty();
                assertThat(errors).hasSize(12);
                assertThat(errors).contains(
                    "Null String must not be null",
                    "Empty Array must not be empty",
                    "Blank String must not be blank",
                    "Non-Numeric String must be numeric",
                    "Not In Range Integer must be between 0.0 and 100.0",
                    "Negative Integer must be positive or zero",
                    "Positive Integer must be negative or zero",
                    "Not Matching Pattern String must match pattern: ^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$",
                    "False Value must be true",
                    "True Value must be false",
                    "Invalid Email must be a valid email address",
                    "Not Required Integer must be equal to zero"
                );
            });
    }

    @Test
    void testValidateAndReturnErrors_WhenNoValidations_ShouldReturnEmptyList() {
        assertThatNoException().isThrownBy(() -> {
            List<String> errors = FluentValidator.collectAll()
                .validateAndReturnErrors();
            assertThat(errors).isEmpty();
        });
    }

    @Test
    void testValidate_WhenNullValue_ShouldNotThrowException() {
        String value = null;

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonNull(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be null");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be null");
    }

    @Test
    void testValidate_WhenOptionalIsEmpty_ShouldThrowException() {
        Optional<String> value = Optional.empty();

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonEmpty(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be empty");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be empty");
    }

    @Test
    void testValidate_WhenOptionalIsPresent_ShouldNotThrowException() {
        Optional<String> value = Optional.of("test");

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNonEmpty(() -> value, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenCharSequenceIsEmpty_ShouldThrowException() {
        CharSequence value = "";

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonEmpty(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be empty");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be empty");
    }

    @Test
    void testValidate_WhenCharSequenceIsNotEmpty_ShouldNotThrowException() {
        CharSequence value = "test";

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNonEmpty(() -> value, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenStringArrayIsEmpty_ShouldThrowException() {
        String[] value = {};

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonEmpty(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be empty");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be empty");
    }

    @Test
    void testValidate_WhenStringArrayIsNotEmpty_ShouldNotThrowException() {
        String[] value = {"test"};

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNonEmpty(() -> value, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenStringListIsEmpty_ShouldThrowException() {
        List<String> value = List.of();

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonEmpty(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be empty");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be empty");
    }

    @Test
    void testValidate_WhenStringListIsNotEmpty_ShouldNotThrowException() {
        List<String> value = List.of("test");

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNonEmpty(() -> value, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenMapIsEmpty_ShouldThrowException() {
        Map<String, String> value = Collections.emptyMap();

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonEmpty(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be empty");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be empty");
    }

    @Test
    void testValidate_WhenMapIsNotEmpty_ShouldNotThrowException() {
        java.util.Map<String, String> value = java.util.Map.of("key", "value");

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNonEmpty(() -> value, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenStringValueIsBlank_ShouldThrowException() {
        String value = "";

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNotBlank(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be blank");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be blank");
    }

    @Test
    void testValidate_WhenStringValueIsNotBlank_ShouldNotThrowException() {
        String value = "test";

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNotBlank(() -> value, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenStringBuilderIsEmpty_ShouldThrowException() {
        StringBuilder value = new StringBuilder();

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonEmpty(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be empty");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be empty");
    }

    @Test
    void testValidate_WhenStringBuilderIsNotEmpty_ShouldNotThrowException() {
        StringBuilder value = new StringBuilder("test");

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNonEmpty(() -> value, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenStringBufferIsEmpty_ShouldThrowException() {
        StringBuffer value = new StringBuffer();

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNonEmpty(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must not be empty");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must not be empty");
    }

    @Test
    void testValidate_WhenStringBufferIsNotEmpty_ShouldNotThrowException() {
        StringBuffer value = new StringBuffer("test");

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNonEmpty(() -> value, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenValueIsNotNumeric_ShouldThrowException() {
        String value = "not a number";

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNumeric(() -> value, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must be numeric");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must be numeric");
    }

    @Test
    void testValidate_WhenValueIsNumeric_ShouldNotThrowException() {
        Byte byteValue = 123;
        Short shortValue = 12345;
        Integer intValue = 123456789;
        Long longValue = 1234567890123456789L;
        Float floatValue = 123.45f;
        Double doubleValue = 123456.789;
        BigDecimal bigDecimalValue = BigDecimal.valueOf(123456789.0123456789);

        byte byteValuePrimitive = 123;
        short shortValuePrimitive = 12345;
        int intValuePrimitive = 123456789;
        long longValuePrimitive = 1234567890123456789L;
        float floatValuePrimitive = 123.45f;
        double doubleValuePrimitive = 123456.789;

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNumeric(() -> byteValue, "Byte Value")
            .requireNumeric(() -> shortValue, "Short Value")
            .requireNumeric(() -> intValue, "Integer Value")
            .requireNumeric(() -> longValue, "Long Value")
            .requireNumeric(() -> floatValue, "Float Value")
            .requireNumeric(() -> doubleValue, "Double Value")
            .requireNumeric(() -> bigDecimalValue, "BigDecimal Value")
            .requireNumeric(() -> byteValuePrimitive, "Byte Value Primitive")
            .requireNumeric(() -> shortValuePrimitive, "Short Value Primitive")
            .requireNumeric(() -> intValuePrimitive, "Integer Value Primitive")
            .requireNumeric(() -> longValuePrimitive, "Long Value Primitive")
            .requireNumeric(() -> floatValuePrimitive, "Float Value Primitive")
            .requireNumeric(() -> doubleValuePrimitive, "Double Value Primitive")
            .validate());
    }

    @Test
    void testValidate_WhenValueIsNotInRange_ShouldThrowException() {
        BigDecimal value = BigDecimal.valueOf(1500.00);

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireInRange(() -> value, 0.0, 1000.0, "Value")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must be between 0.0 and 1000.0");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must be between 0.0 and 1000.0");
    }

    @Test
    void testValidate_WhenValueIsInRange_ShouldNotThrowException() {
        BigDecimal value = BigDecimal.valueOf(500.00);

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireInRange(() -> value, 0.0, 1000.0, "Value")
            .validate());
    }

    @Test
    void testValidate_WhenValueIsNotPositiveOrZero_ShouldThrowException() {
        Byte byteValue = -5;
        Short shortValue = -10;
        Integer intValue = -20;
        Long longValue = -100L;
        Float floatValue = -15.5f;
        Double doubleValue = -30.0;
        BigDecimal bigDecimalValue = BigDecimal.valueOf(-50.0);

        byte byteValuePrimitive = -5;
        short shortValuePrimitive = -10;
        int intValuePrimitive = -20;
        long longValuePrimitive = -100L;
        float floatValuePrimitive = -15.5f;
        double doubleValuePrimitive = -30.0;

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requirePositiveOrZero(() -> byteValue, "Byte Value")
                .requirePositiveOrZero(() -> shortValue, "Short Value")
                .requirePositiveOrZero(() -> intValue, "Integer Value")
                .requirePositiveOrZero(() -> longValue, "Long Value")
                .requirePositiveOrZero(() -> floatValue, "Float Value")
                .requirePositiveOrZero(() -> doubleValue, "Double Value")
                .requirePositiveOrZero(() -> bigDecimalValue, "BigDecimal Value")
                .requirePositiveOrZero(() -> byteValuePrimitive, "Byte Value Primitive")
                .requirePositiveOrZero(() -> shortValuePrimitive, "Short Value Primitive")
                .requirePositiveOrZero(() -> intValuePrimitive, "Integer Value Primitive")
                .requirePositiveOrZero(() -> longValuePrimitive, "Long Value Primitive")
                .requirePositiveOrZero(() -> floatValuePrimitive, "Float Value Primitive")
                .requirePositiveOrZero(() -> doubleValuePrimitive, "Double Value Primitive")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: "
            + "Byte Value must be positive or zero, "
            + "Short Value must be positive or zero, "
            + "Integer Value must be positive or zero, "
            + "Long Value must be positive or zero, "
            + "Float Value must be positive or zero, "
            + "Double Value must be positive or zero, "
            + "BigDecimal Value must be positive or zero, "
            + "Byte Value Primitive must be positive or zero, "
            + "Short Value Primitive must be positive or zero, "
            + "Integer Value Primitive must be positive or zero, "
            + "Long Value Primitive must be positive or zero, "
            + "Float Value Primitive must be positive or zero, "
            + "Double Value Primitive must be positive or zero"
        );

        assertThat(exception.getErrors()).isNotEmpty();

        assertThat(exception.getErrors()).contains(
            "Byte Value must be positive or zero",
            "Short Value must be positive or zero",
            "Integer Value must be positive or zero",
            "Long Value must be positive or zero",
            "Float Value must be positive or zero",
            "Double Value must be positive or zero",
            "BigDecimal Value must be positive or zero",
            "Byte Value Primitive must be positive or zero",
            "Short Value Primitive must be positive or zero",
            "Integer Value Primitive must be positive or zero",
            "Long Value Primitive must be positive or zero",
            "Float Value Primitive must be positive or zero",
            "Double Value Primitive must be positive or zero"
        );
    }

    @Test
    void testValidate_WhenValueIsPositiveOrZero_ShouldNotThrowException() {
        Byte byteValue = 5;
        Short shortValue = 10;
        Integer intValue = 20;
        Long longValue = 100L;
        Float floatValue = 15.5f;
        Double doubleValue = 30.0;
        BigDecimal bigDecimalValue = BigDecimal.valueOf(50.0);

        byte byteValuePrimitive = 5;
        short shortValuePrimitive = 10;
        int intValuePrimitive = 20;
        long longValuePrimitive = 100L;
        float floatValuePrimitive = 15.5f;
        double doubleValuePrimitive = 30.0;

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requirePositiveOrZero(() -> byteValue, "Byte Value")
            .requirePositiveOrZero(() -> shortValue, "Short Value")
            .requirePositiveOrZero(() -> intValue, "Integer Value")
            .requirePositiveOrZero(() -> longValue, "Long Value")
            .requirePositiveOrZero(() -> floatValue, "Float Value")
            .requirePositiveOrZero(() -> doubleValue, "Double Value")
            .requirePositiveOrZero(() -> bigDecimalValue, "BigDecimal Value")
            .requirePositiveOrZero(() -> byteValuePrimitive, "Byte Value Primitive")
            .requirePositiveOrZero(() -> shortValuePrimitive, "Short Value Primitive")
            .requirePositiveOrZero(() -> intValuePrimitive, "Integer Value Primitive")
            .requirePositiveOrZero(() -> longValuePrimitive, "Long Value Primitive")
            .requirePositiveOrZero(() -> floatValuePrimitive, "Float Value Primitive")
            .requirePositiveOrZero(() -> doubleValuePrimitive, "Double Value Primitive")
            .validate());
    }

    @Test
    void testValidate_WhenValueIsNotNegativeOrZero_ShouldThrowException() {
        Byte byteValue = 5;
        Short shortValue = 10;
        Integer intValue = 20;
        Long longValue = 100L;
        Float floatValue = 15.5f;
        Double doubleValue = 30.0;
        BigDecimal bigDecimalValue = BigDecimal.valueOf(50.0);

        byte byteValuePrimitive = 5;
        short shortValuePrimitive = 10;
        int intValuePrimitive = 20;
        long longValuePrimitive = 100L;
        float floatValuePrimitive = 15.5f;
        double doubleValuePrimitive = 30.0;

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireNegativeOrZero(() -> byteValue, "Byte Value")
                .requireNegativeOrZero(() -> shortValue, "Short Value")
                .requireNegativeOrZero(() -> intValue, "Integer Value")
                .requireNegativeOrZero(() -> longValue, "Long Value")
                .requireNegativeOrZero(() -> floatValue, "Float Value")
                .requireNegativeOrZero(() -> doubleValue, "Double Value")
                .requireNegativeOrZero(() -> bigDecimalValue, "BigDecimal Value")
                .requireNegativeOrZero(() -> byteValuePrimitive, "Byte Value Primitive")
                .requireNegativeOrZero(() -> shortValuePrimitive, "Short Value Primitive")
                .requireNegativeOrZero(() -> intValuePrimitive, "Integer Value Primitive")
                .requireNegativeOrZero(() -> longValuePrimitive, "Long Value Primitive")
                .requireNegativeOrZero(() -> floatValuePrimitive, "Float Value Primitive")
                .requireNegativeOrZero(() -> doubleValuePrimitive, "Double Value Primitive")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: "
            + "Byte Value must be negative or zero, "
            + "Short Value must be negative or zero, "
            + "Integer Value must be negative or zero, "
            + "Long Value must be negative or zero, "
            + "Float Value must be negative or zero, "
            + "Double Value must be negative or zero, "
            + "BigDecimal Value must be negative or zero, "
            + "Byte Value Primitive must be negative or zero, "
            + "Short Value Primitive must be negative or zero, "
            + "Integer Value Primitive must be negative or zero, "
            + "Long Value Primitive must be negative or zero, "
            + "Float Value Primitive must be negative or zero, "
            + "Double Value Primitive must be negative or zero"
        );

        assertThat(exception.getErrors()).isNotEmpty();

        assertThat(exception.getErrors()).contains(
            "Byte Value must be negative or zero",
            "Short Value must be negative or zero",
            "Integer Value must be negative or zero",
            "Long Value must be negative or zero",
            "Float Value must be negative or zero",
            "Double Value must be negative or zero",
            "BigDecimal Value must be negative or zero",
            "Byte Value Primitive must be negative or zero",
            "Short Value Primitive must be negative or zero",
            "Integer Value Primitive must be negative or zero",
            "Long Value Primitive must be negative or zero",
            "Float Value Primitive must be negative or zero",
            "Double Value Primitive must be negative or zero"
        );
    }

    @Test
    void testValidate_WhenValueIsNegativeOrZero_ShouldNotThrowException() {
        Byte byteValue = -5;
        Short shortValue = -10;
        Integer intValue = -20;
        Long longValue = -100L;
        Float floatValue = -15.5f;
        Double doubleValue = -30.0;
        BigDecimal bigDecimalValue = BigDecimal.valueOf(-50.0);

        byte byteValuePrimitive = -5;
        short shortValuePrimitive = -10;
        int intValuePrimitive = -20;
        long longValuePrimitive = -100L;
        float floatValuePrimitive = -15.5f;
        double doubleValuePrimitive = -30.0;

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireNegativeOrZero(() -> byteValue, "Byte Value")
            .requireNegativeOrZero(() -> shortValue, "Short Value")
            .requireNegativeOrZero(() -> intValue, "Integer Value")
            .requireNegativeOrZero(() -> longValue, "Long Value")
            .requireNegativeOrZero(() -> floatValue, "Float Value")
            .requireNegativeOrZero(() -> doubleValue, "Double Value")
            .requireNegativeOrZero(() -> bigDecimalValue, "BigDecimal Value")
            .requireNegativeOrZero(() -> byteValuePrimitive, "Byte Value Primitive")
            .requireNegativeOrZero(() -> shortValuePrimitive, "Short Value Primitive")
            .requireNegativeOrZero(() -> intValuePrimitive, "Integer Value Primitive")
            .requireNegativeOrZero(() -> longValuePrimitive, "Long Value Primitive")
            .requireNegativeOrZero(() -> floatValuePrimitive, "Float Value Primitive")
            .requireNegativeOrZero(() -> doubleValuePrimitive, "Double Value Primitive")
            .validate());
    }

    @Test
    void testValidate_WhenValueDoesNotMatchPattern_ShouldThrowException() {
        String value = "invalid-email";

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireMatches(() -> value, "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$", "Email format")
                .validate()).actual();

        assertThat(exception).hasMessage(
            "Validation failed: Email format must match pattern: ^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains(
            "Email format must match pattern: ^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$");
    }

    @Test
    void testValidate_WhenValueMatchesPattern_ShouldNotThrowException() {
        String value = "test@example.com";
        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireMatches(() -> value, "^[\\w-\\.]+@[\\w-]+\\.[a-z]{2,4}$", "Email format")
            .validate());
    }

    @Test
    void testValidate_WhenRequireTrueHasFalseValue_ShouldThrowException() {
        boolean condition = false;

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireTrue(() -> condition, "Condition")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Condition must be true");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Condition must be true");
    }

    @Test
    void testValidate_WhenRequireTrueHasTrueValue_ShouldNotThrowException() {
        boolean condition = true;

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireTrue(() -> condition, "Condition")
            .validate());
    }

    @Test
    void testValidate_WhenRequireFalseHasTrueValue_ShouldThrowException() {
        boolean condition = true;

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireFalse(() -> condition, "Condition")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Condition must be false");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Condition must be false");
    }

    @Test
    void testValidate_WhenRequireFalseHasFalseValue_ShouldNotThrowException() {
        boolean condition = false;

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireFalse(() -> condition, "Condition")
            .validate());
    }

    @Test
    void testValidate_WhenValueIsNotValidEmail_ShouldThrowException() {
        String email = "invalid-email";

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireValidEmail(() -> email, "Email")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Email must be a valid email address");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Email must be a valid email address");
    }

    @Test
    void testValidate_WhenValueIsValidEmail_ShouldNotThrowException() {
        String email = "test@example.com";
        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireValidEmail(() -> email, "Email")
            .validate());
    }

    @Test
    void testValidate_WhenRequireThatConditionIsFalse_ShouldThrowException() {
        int value = 5;

        ValidationException exception = assertThatExceptionOfType(ValidationException.class)
            .isThrownBy(() -> FluentValidator.collectAll()
                .requireThat(() -> value, v -> v > 10, "Value", "Value must be greater than 10")
                .validate()).actual();

        assertThat(exception).hasMessage("Validation failed: Value must be greater than 10");
        assertThat(exception.getErrors()).isNotEmpty();
        assertThat(exception.getErrors()).contains("Value must be greater than 10");
    }

    @Test
    void testValidate_WhenRequireThatConditionIsTrue_ShouldNotThrowException() {
        int value = 15;

        assertThatNoException().isThrownBy(() -> FluentValidator.collectAll()
            .requireThat(() -> value, v -> v > 10, "Value", "Value must be greater than 10")
            .validate());
    }
}