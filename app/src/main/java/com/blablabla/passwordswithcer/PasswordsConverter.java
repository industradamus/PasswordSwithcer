package com.blablabla.passwordswithcer;

import java.util.HashMap;
import java.util.Objects;

class PasswordsConverter {

    private HashMap<String, String> dictionary;

    PasswordsConverter(String[] russians, String[] latin) {
        dictionary = new HashMap<>();
        createDictionary(russians, latin);
    }

    String convertRussianToLatin(CharSequence source) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < source.length(); i++) {
            char c = source.charAt(i);
            String s = String.valueOf(c).toLowerCase();
            if (!dictionary.containsKey(s)) {
                result.append(s);
            } else if (Character.isUpperCase(c)) {
                result.append(Objects.requireNonNull(dictionary.get(s)).toUpperCase());
            } else {
                result.append(dictionary.get(s));
            }
        }
        return result.toString();
    }

    private void createDictionary(String[] russians, String[] latin) {
        if (russians.length != latin.length) {
            throw new IllegalArgumentException();
        } else {
            for (int i = 0; i < russians.length; i++) {
                dictionary.put(russians[i], latin[i]);
            }
        }
    }
}
