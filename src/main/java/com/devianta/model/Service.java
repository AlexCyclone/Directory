package com.devianta.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Service {
    static String safeTrimEmptyToNull(String str) {
        if (str == null) {
            return null;
        }
        str = str.trim();
        return str.length() == 0 ? null : str;
    }

    static String safeTrimNullToEmpty(String str) {
        if (str == null) {
            return "";
        }
        return str.trim();
    }

    static boolean containNull(Object... obj) {
        for (Object o : obj) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    static boolean nullOrLimit(int lowerLimit, int upperLimit, String... str) {
        for (String s : str) {
            if (s == null || s.length() < lowerLimit || s.length() > upperLimit) {
                return true;
            }
        }
        return false;
    }

    static boolean allClearOrLimit(int lowerLimit, int upperLimit, String... str) {
        int summaryLength = 0;
        for (String s : str) {
            if (s == null) {
                continue;
            }
            if (s.length() < lowerLimit || s.length() > upperLimit) {
                return true;
            }
            summaryLength += s.length();
        }
        return summaryLength == 0;
    }

    static boolean patternMatch(String str, String pattern) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        return m.matches();
    }
}
