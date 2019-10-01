package com.blablabla.passwordswithcer;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PasswordGeneratorTest {

    private static final int LOWER_CASE = 0;
    private static final int UPPER_CASE = 1;
    private static final int DIGITS = 2;
    private static final int SPECIAL_SYMBOL = 3;

    private List<Integer> lowerAndUpper = new ArrayList<>();
    private List<Integer> digitAndSpecials = new ArrayList<>();
    private List<Integer> lowerAndDigits = new ArrayList<>();
    private List<Integer> allTypes = new ArrayList<>();

    private PasswordGenerator passwordGenerator;

    private int length;
    private String lowerDigitPassword;
    private String digitSpecialPassword;
    private String lowerUpperPassword;
    private String allTypesPassword;

    @Before
    public void setUp() {
        passwordGenerator = new PasswordGenerator();

        lowerAndUpper.add(LOWER_CASE);
        lowerAndUpper.add(UPPER_CASE);

        digitAndSpecials.add(DIGITS);
        digitAndSpecials.add(SPECIAL_SYMBOL);

        lowerAndDigits.add(DIGITS);
        lowerAndDigits.add(LOWER_CASE);

        allTypes.add(LOWER_CASE);
        allTypes.add(UPPER_CASE);
        allTypes.add(DIGITS);
        allTypes.add(SPECIAL_SYMBOL);

        length = 25;
        lowerDigitPassword = passwordGenerator.generatePassword(length, lowerAndDigits);
        digitSpecialPassword = passwordGenerator.generatePassword(length, digitAndSpecials);
        lowerUpperPassword = passwordGenerator.generatePassword(length, lowerAndUpper);
        allTypesPassword = passwordGenerator.generatePassword(length, allTypes);
    }

    @Test
    public void generatePassword() {
        checkLength();
        checkLowerAndDigit();
        checkDigitAndSpecial();
        checkLowerAndUpper();
        checkAllTypes();
    }

    @Test
    public void getPasswordDifficult() {
        assertEquals(4, PasswordGenerator.getPasswordDifficult(allTypesPassword));
        assertEquals(3, PasswordGenerator.getPasswordDifficult(lowerDigitPassword));
        assertEquals(3, PasswordGenerator.getPasswordDifficult(digitSpecialPassword));
        assertEquals(3, PasswordGenerator.getPasswordDifficult(lowerDigitPassword));
    }

    private void checkLength() {
        assertEquals(length, passwordGenerator.generatePassword(length, lowerAndDigits).length());
    }

    private void checkLowerAndDigit() {
        assertTrue(checkDigits(lowerDigitPassword)
                && checkLowerCase(lowerDigitPassword));
    }

    private void checkDigitAndSpecial() {
        assertTrue(checkDigits(digitSpecialPassword)
                && checkSpecialSymbol(digitSpecialPassword));
    }

    private void checkLowerAndUpper() {
        assertTrue(checkUpperCase(lowerUpperPassword)
                && checkLowerCase(lowerUpperPassword));
    }

    private void checkAllTypes() {
        assertTrue(checkDigits(allTypesPassword)
                && checkLowerCase(allTypesPassword)
                && checkUpperCase(allTypesPassword)
                && checkSpecialSymbol(allTypesPassword));
    }

    private boolean checkLowerCase(String password) {
        boolean hasLower = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isLowerCase(password.charAt(i))) {
                hasLower = true;
                break;
            }
        }
        return hasLower;
    }

    private boolean checkUpperCase(String password) {
        boolean hasUpper = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                hasUpper = true;
                break;
            }
        }
        return hasUpper;
    }

    private boolean checkSpecialSymbol(String password) {
        return !password.matches("[^A-Za-z0-9 ]");
    }

    private boolean checkDigits(String password) {
        boolean hasDigits = false;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                hasDigits = true;
                break;
            }
        }
        return hasDigits;
    }
}