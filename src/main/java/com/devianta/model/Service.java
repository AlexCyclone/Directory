package com.devianta.model;

public interface Service {
    static String safeTrim(String str) {
        if (str == null) {
            return str;
        }
        return str.trim();
    }

    static Boolean defaultTrue(Boolean b) {
        if (b == null) {
            return true;
        }
        return false;
    }

    static boolean containNull(Object... obj) {
        for(Object o : obj) {
            if (o == null) {
                return true;
            }
        }
        return false;
    }

    static boolean containEmptyOrLimit(int limit, String... str) {
        for (String s : str) {
            if (s == null || s.equals("") || s.length() > limit) {
                return true;
            }
        }
        return false;
    }

    static boolean containAlongEmptyOrLimit(int limit, String... str) {
        int fillCounter = 0;
        for (String s : str) {
            if (s == null || s.equals("")) {
                continue;
            }
            if (s.length() > limit) {
                return true;
            }
            fillCounter++;
        }
        if (fillCounter == 0) {
            return true;
        }
        return false;
    }
}
