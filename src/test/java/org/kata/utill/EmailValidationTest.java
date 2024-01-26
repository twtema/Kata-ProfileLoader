package org.kata.utill;

import org.apache.commons.validator.routines.EmailValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class EmailValidationTest {

    @InjectMocks
    EmailValidation emailValidation;

    @InjectMocks
    EmailValidator emailValidator = EmailValidator.getInstance();

    List<String> goodEmails = Arrays.asList("username@domain.com", "user-name@domain.com", "User.name@domain.com.net");
    List<String> badEmails = Arrays.asList("username.@domain.com", ".user.name@domain.com", "username@.com", "vebad91429@grassdev.com");

    @Test
    public void validateEmail() {
        boolean i = true;
        for (String email : goodEmails) {
            if (!emailValidator.isValid(email) || emailValidation.blacklistEmails.contains(email)) {
                i = false;
            }
        }
        Assertions.assertTrue(i);

        for (String email : badEmails) {
            if (!emailValidator.isValid(email) || emailValidation.blacklistEmails.contains(email)) {
                i = false;
            }
        }
        Assertions.assertFalse(i);

    }

}