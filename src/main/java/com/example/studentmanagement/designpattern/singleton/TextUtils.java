package com.example.studentmanagement.designpattern.singleton;

import java.text.Normalizer;

public class TextUtils {

    // Singleton với Inner Static Holder
    private TextUtils() {
    }

    private static class Holder {

        private static final TextUtils INSTANCE = new TextUtils();
    }

    public static TextUtils getInstance() {
        return Holder.INSTANCE;
    }

    // Phương thức loại bỏ dấu
    public String removeDiacritics(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }

    public boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    public String toLowerCaseNoDiacritics(String input) {
        if (input == null) {
            return null;
        }
        return removeDiacritics(input).toLowerCase();
    }
}
