package com.banfico.model;

public enum RequestSource {
    SINGLE("SINGLE"), BATCH("BATCH");

    private String value;

    RequestSource(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static RequestSource fromValue(String text) {
        for (RequestSource source : RequestSource.values()) {
            if (String.valueOf(source.value).equals(text)) {
                return source;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + text + "'");
    }
}
