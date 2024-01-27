package org.kata.util;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.kata.exception.InvalidPhoneNumberException;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.fail;

@SpringBootTest
@RunWith(JUnitParamsRunner.class)
public class PhoneNumberValidatorTest {

    @Test
    @Parameters({
            "+7 (999) 123-45-67",
            "8-915-678-90-12",
            "+7(495)1234567"
    })
    public void testValidPhoneNumbers(String phoneNumber) {
        try {
            PhoneNumberValidator.validatePhoneNumbers(phoneNumber);
        } catch (InvalidPhoneNumberException e) {
            fail(e.getMessage());
        }
    }

    @Test
    @Parameters({
            "(929)657+04",
            "8(929)-64-34",
            "8-999-123"
    })
    public void testInvalidPhoneNumbers(String phoneNumber) {
        try {
            PhoneNumberValidator.validatePhoneNumbers(phoneNumber);
            fail("Expected InvalidPhoneNumberException");
        } catch (InvalidPhoneNumberException ignored) {
        }
    }
}
