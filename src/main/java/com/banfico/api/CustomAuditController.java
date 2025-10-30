package com.banfico.api;

import com.banfico.dto.AuditDto;
import com.banfico.model.RequestType;
import com.banfico.model.TransactionFilter;
import com.banfico.service.AuditService;
import com.banfico.service.CustomAuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/v1.0/admin/custom-audits")
@RestController
public class CustomAuditController {

    private static final String DEFAULT_SORT_VALUE = "requestTimeReceived";

    @Autowired
    private CustomAuditService auditService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AuditDto> searchAudit(
            @RequestParam(value = "assigneeBic", required = false) String bic,
            @RequestParam(value = "iban", required = false) String iban,
            @RequestParam(value = "requestedOrgName", required = false) String requestedOrgName,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "amount", required = false) BigDecimal amount,
            @RequestParam(value = "currency", required = false) String currency,
            @RequestParam(name = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromTime,
            @RequestParam(name = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toTime,
            @PageableDefault(sort = {DEFAULT_SORT_VALUE}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        TransactionFilter transactionFilter = new TransactionFilter(bic, iban, requestedOrgName, userId, clientId, amount, currency,
                fromTime, toTime);
        return auditService.search(transactionFilter, pageable);
    }

}
