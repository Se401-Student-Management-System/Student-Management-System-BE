package com.example.studentmanagement.config;

import java.text.Normalizer;

public class TextUtils {
    public static String removeDiacritics(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
