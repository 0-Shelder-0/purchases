package com.example.purchases.extensions;

public class StringExtensions {

    public static boolean isNullOrEmpty(String value) {
        return value == null || value.isBlank();
    }
}
