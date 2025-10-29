package com.banfico.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ImportRequestStatus {
    IN_PROGRESS("IN_PROGRESS"),
    SUCCESS("SUCCESS"),
    FAILURE("FAILURE");

    private final String value;

    @JsonCreator
    public static ImportRequestStatus fromString(String text) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(text))
                .findFirst()
                .orElse(null);
    }
}

