package com.banfico.api;

import com.banfico.dto.AuditDto;
import com.banfico.model.RequestType;
import com.banfico.service.AuditService;
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

import java.time.LocalDateTime;
import java.util.List;

@RequestMapping("/api/v1.0/admin/audits")
@RestController
public class AuditController {

    private static final String DEFAULT_SORT_VALUE = "requestTimeReceived";

    @Autowired
    private AuditService auditService;

    @GetMapping(path = "/slice", produces = MediaType.APPLICATION_JSON_VALUE)
    public Slice<AuditDto> searchAuditWithSlice(
            @RequestParam(value = "bic", required = false) String bic,
            @RequestParam(value = "iban", required = false) String iban,
            @RequestParam(value = "requestType", required = false) RequestType requestType,
            @RequestParam(value = "responseMatched", required = false) Boolean responseMatched,
            @RequestParam(value = "responseReasonCode", required = false) List<String> reasonCode,
            @RequestParam(value = "idType", required = false) List<String> idType,
            @RequestParam(value = "idValue", required = false) String idValue,
            @RequestParam(value = "requestedOrgName", required = false) List<String> requestedOrgName,
            @RequestParam(value = "accountName", required = false) String accountName,
            @RequestParam(value = "accountId", required = false) String accountId,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "batchId", required = false) String batchId,
            @RequestParam(value = "transactionId", required = false) String transactionId,
            @RequestParam(value = "requestSource", required = false) String requestSource,
            @RequestParam(name = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromTime,
            @RequestParam(name = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toTime,
            @PageableDefault(sort = {DEFAULT_SORT_VALUE}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return auditService.searchAndReturnWithSlice(bic, iban, requestType, responseMatched, reasonCode, idType, idValue, requestedOrgName,
                accountName, accountId, userId, clientId, batchId, transactionId, requestSource, fromTime, toTime, pageable);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AuditDto> searchAudit(
            @RequestParam(value = "bic", required = false) String bic,
            @RequestParam(value = "iban", required = false) String iban,
            @RequestParam(value = "requestType", required = false) RequestType requestType,
            @RequestParam(value = "responseMatched", required = false) Boolean responseMatched,
            @RequestParam(value = "responseReasonCode", required = false) List<String> reasonCode,
            @RequestParam(value = "idType", required = false) List<String> idType,
            @RequestParam(value = "idValue", required = false) String idValue,
            @RequestParam(value = "requestedOrgName", required = false) List<String> requestedOrgName,
            @RequestParam(value = "accountName", required = false) String accountName,
            @RequestParam(value = "accountId", required = false) String accountId,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "batchId", required = false) String batchId,
            @RequestParam(value = "transactionId", required = false) String transactionId,
            @RequestParam(value = "requestSource", required = false) String requestSource,
            @RequestParam(name = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromTime,
            @RequestParam(name = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toTime,
            @PageableDefault(sort = {DEFAULT_SORT_VALUE}, direction = Sort.Direction.DESC) Pageable pageable
    ) {

        return auditService.search(bic, iban, requestType, responseMatched, reasonCode, idType, idValue, requestedOrgName,
                accountName, accountId, userId, clientId, batchId, transactionId, requestSource, fromTime, toTime, pageable);
    }

}
