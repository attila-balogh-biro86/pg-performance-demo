package com.banfico.model;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record TransactionFilter(
        String assigneeBic,
        String iban,
        String requestedOrgName,
        String userId,
        String clientId,
        BigDecimal amount,
        String currency,
        OffsetDateTime requestTimeReceivedStartFrom,
        OffsetDateTime requestTimeReceivedStartTo
) {}

