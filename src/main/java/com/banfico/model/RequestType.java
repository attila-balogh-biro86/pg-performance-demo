package com.banfico.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum RequestType {
    CAR("CAR"), //Confirmation of Account Request
    CPR("CPR"); //Confirmation of Payee Request

    @JsonValue
    private final String value;

    @JsonCreator
    public static RequestType fromString(String text) {
        return Arrays.stream(values()).filter(value -> value.getValue().equalsIgnoreCase(text))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unexpected value '" + text + "'"));
    }
}

