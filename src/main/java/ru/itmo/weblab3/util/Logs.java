package ru.itmo.weblab3.util;

public final class Logs {

    private Logs() {
    }

    // убирает переводы строк, чтобы пользователь не мог подделать записи журнала
    public static String clean(String value) {
        if (value == null) {
            return "null";
        }

        return value.replaceAll("[\\r\\n\\t]", "_");
    }
}
