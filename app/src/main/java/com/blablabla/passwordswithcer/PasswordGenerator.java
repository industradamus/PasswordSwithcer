package com.blablabla.passwordswithcer;

import java.util.List;
import java.util.Random;

class PasswordGenerator {

    private static final int A_ASCII_UPPERCASE_VALUE = 65;
    private static final int Z_ASCII_UPPERCASE_VALUE = 90;
    private static final int A_ASCII_LOWERCASE_VALUE = 97;
    private static final int Z_ASCII_LOWERCASE_VALUE = 122;
    private static final int ZERO_ASCII_VALUE = 48;
    private static final int NINE_ASCII_VALUE = 57;
    private static final int START_SYMBOL_ASCII_VALUE = 33;
    private static final int END_SYMBOL_ASCII_VALUE = 47;

    static final int LOWER_CASE = 0;
    static final int UPPER_CASE = 1;
    static final int DIGITS = 2;
    static final int SPECIAL_SYMBOL = 3;

    String generatePassword(int length, List<Integer> checkedTypes) {
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int key = random.nextInt(checkedTypes.size());
            char randomSymbol = 0;
            switch (checkedTypes.get(key)) {
                case LOWER_CASE:
                    randomSymbol = generateLowerCaseSymbol();
                    break;
                case UPPER_CASE:
                    randomSymbol = generateUpperCaseSymbol();
                    break;
                case DIGITS:
                    randomSymbol = generateDigitsSymbol();
                    break;
                case SPECIAL_SYMBOL:
                    randomSymbol = generateSpecialSymbol();
                    break;
            }
            buffer.append(randomSymbol);
        }
        return buffer.toString();
    }

    static int getPasswordDifficult(String password) {
        boolean hasUpperCase = false;
        boolean hasDigits = false;
        boolean hasSpecialSymbols = !password.matches("[^A-Za-z0-9 ]");
        boolean hasSafeLength = password.length() >= 8;

        int difficult = 0;

        for (int i = 0; i < password.length(); i++) {
            char pass = password.charAt(i);
            if (Character.isDigit(pass)) hasDigits = true;
            if (Character.isUpperCase(pass)) hasUpperCase = true;
            if (hasDigits && hasUpperCase) break;
        }

        if (hasUpperCase) difficult++;
        if (hasDigits) difficult++;
        if (hasSpecialSymbols) difficult++;
        if (hasSafeLength) difficult++;
        return difficult;
    }

    private char generateLowerCaseSymbol() {
        Random random = new Random();
        int randomLimitedInt = A_ASCII_LOWERCASE_VALUE + (int) (random.nextFloat() *
                (Z_ASCII_LOWERCASE_VALUE - A_ASCII_LOWERCASE_VALUE + 1));
        return (char) randomLimitedInt;
    }

    private char generateUpperCaseSymbol() {
        Random random = new Random();
        int randomLimitedInt = A_ASCII_UPPERCASE_VALUE + (int) (random.nextFloat() *
                (Z_ASCII_UPPERCASE_VALUE - A_ASCII_UPPERCASE_VALUE + 1));
        return (char) randomLimitedInt;
    }

    private char generateDigitsSymbol() {
        Random random = new Random();
        int randomLimitedInt = ZERO_ASCII_VALUE + (int) (random.nextFloat() *
                (NINE_ASCII_VALUE - ZERO_ASCII_VALUE + 1));
        return (char) randomLimitedInt;
    }

    private char generateSpecialSymbol() {
        Random random = new Random();
        int randomLimitedInt = START_SYMBOL_ASCII_VALUE + (int) (random.nextFloat() *
                (END_SYMBOL_ASCII_VALUE - START_SYMBOL_ASCII_VALUE + 1));
        return (char) randomLimitedInt;
    }
}
