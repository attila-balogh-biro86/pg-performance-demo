package com.banfico.api;

import com.banfico.dto.AuditDto;
import com.banfico.dto.KeysetPageResponse;
import com.banfico.model.RequestAudit;
import com.banfico.model.TransactionFilter;
import com.banfico.serialization.KeySetSerializationUtil;
import com.banfico.service.BlazeAuditService;
import com.blazebit.persistence.DefaultKeyset;
import com.blazebit.persistence.DefaultKeysetPage;
import com.blazebit.persistence.Keyset;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.spring.data.base.query.KeysetAwarePageImpl;
import com.blazebit.persistence.spring.data.repository.KeysetAwarePage;
import com.blazebit.persistence.spring.data.repository.KeysetPageRequest;
import com.blazebit.persistence.spring.data.webmvc.KeysetConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@RequestMapping("/api/v1.0/admin/audits")
@RestController
public class AuditController {

    private static final String DEFAULT_SORT_VALUE = "requestTimeReceived";

    @Autowired
    private BlazeAuditService auditService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public KeysetAwarePage<RequestAudit> searchAudit(
            @RequestParam(value = "lowKeySet", required = false) String lowestKeySet,
            @RequestParam(value = "highKeySet", required = false) String highestKeySet,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "assigneeBic", required = false) String bic,
            @RequestParam(value = "iban", required = false) String iban,
            @RequestParam(value = "requestedOrgName", required = false) String requestedOrgName,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "clientId", required = false) String clientId,
            @RequestParam(value = "amount", required = false) BigDecimal amount,
            @RequestParam(value = "currency", required = false) String currency,
            @RequestParam(name = "startTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime fromTime,
            @RequestParam(name = "endTime", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime toTime
    ) {

        TransactionFilter transactionFilter = new TransactionFilter(bic, iban, requestedOrgName, userId, clientId, amount, currency,
                fromTime, toTime);

        KeysetPage keysetPage = createKeysetPage(lowestKeySet, highestKeySet, page, size);
        PagedList<RequestAudit> result = auditService.search(transactionFilter, page, size, keysetPage);
        return new KeysetAwarePageImpl<>(result, Pageable.ofSize(result.size()));
    }


    private KeysetPage createKeysetPage(String lowestKeySet, String highestKeySet, int page, int size) {
        if(lowestKeySet == null || highestKeySet == null) {
            return null;
        }
        Serializable[] lowestTuple = KeySetSerializationUtil.deserialize(lowestKeySet);
        lowestTuple[1] = OffsetDateTime.parse(lowestTuple[1].toString());
        Serializable[] highestTuple = KeySetSerializationUtil.deserialize(highestKeySet);
        highestTuple[1] = OffsetDateTime.parse(highestTuple[1].toString());
        Keyset low = new DefaultKeyset(lowestTuple);
        Keyset high = new DefaultKeyset(highestTuple);
        return new DefaultKeysetPage(page, size, low , high);
    }

}
