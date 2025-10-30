package com.banfico.model;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record TransactionFilter(
        String assigneeBic,
        String iban,
        String requestedOrgName,
        String userId,
        String clientId,
        BigDecimal amount,
        String currency,
        LocalDate requestTimeReceivedStartFrom,
        LocalDate requestTimeReceivedStartTo
) {}

