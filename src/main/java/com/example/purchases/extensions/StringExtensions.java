package com.example.purchases.extensions;

public class StringExtensions {

    public static boolean isNullOrWhiteSpace(String value) {
        return value == null || value.trim().length() == 0;
    }
}
