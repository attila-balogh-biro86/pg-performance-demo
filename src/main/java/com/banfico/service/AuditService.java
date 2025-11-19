package com.banfico.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import com.banfico.dto.AuditDto;
import com.banfico.model.RequestAudit;
import com.banfico.model.RequestType;
import com.banfico.repo.RequestAuditRepository;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.data.jpa.domain.Specification.where;

@Service
@Slf4j
public class AuditService {

    private static final String TRANSACTION_ID = "id";
    private static final String DEFAULT_SORT_VALUE = "requestTimeReceived";
    private static final String BIC_VALUE = "assigneeBic";
    private static final String IBAN_VALUE = "iban";
    private static final String IDENTIFICATION_TYPE = "identificationType";
    private static final String IDENTIFICATION_VALUE = "identificationValue";
    private static final String REQUEST_TYPE_VALUE = "requestType";
    private static final String RESPONSE_MATCHED_VALUE = "responseMatched";
    private static final String RESPONSE_REASON_CODE_VALUE = "responseReasonCode";
    private static final String REQUESTED_ORG_NAME_VALUE = "requestedOrgName";
    private static final String USER_ID_VALUE = "userId";
    private static final String PARTY_ID_MATCH = "partyIdMatch";
    private static final String ID_TYPE = "identificationType";
    private static final String ID_VALUE = "identificationValue";
    private static final String BATCH_ID = "batchId";
    private static final String CLIENT_ID_VALUE = "clientId";
    private static final String ACCOUNT_NAME = "name";
    private static final String REQUEST_SOURCE = "requestSource";
    private static final String VERIFICATION_TYPE = "verificationTypes";

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private RequestAuditRepository requestAuditRepository;


    public Page<AuditDto> search(
            String bic,
            String iban,
            RequestType requestType,
            Boolean responseMatched,
            List<String> reasonCode,
            List<String> idType,
            String idValue,
            List<String> requestedOrgNames,
            String accountName,
            String accountId,
            String userId,
            String clientId,
            String batchId,
            String transactionId,
            String requestSource,
            LocalDate fromTime,
            LocalDate toTime,
            Pageable pageable
    ) {
        Specification<RequestAudit> specification = where((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!StringUtils.isEmpty(bic)) {
                predicates.add(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(BIC_VALUE)), bic.toLowerCase(Locale.ROOT)));
            }

            if (!StringUtils.isEmpty(iban)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(IBAN_VALUE)),
                        iban.toLowerCase(Locale.ROOT)));
            }

            if (!StringUtils.isEmpty(batchId)) {
                predicates.add(criteriaBuilder.equal(root.get(BATCH_ID), batchId));
            }

            if (requestType != null) {
                predicates.add(criteriaBuilder.equal(root.get(REQUEST_TYPE_VALUE), requestType));
            }

            if (responseMatched != null) {
                predicates.add(criteriaBuilder.equal(root.get(RESPONSE_MATCHED_VALUE), responseMatched));
            }

            if (reasonCode != null && !reasonCode.isEmpty()) {
                List<Predicate> reasonCodePredicates = new ArrayList<>();

                List<String> nonNullCodes = new ArrayList<>();
                for (String code : reasonCode) {
                    if ("Invalid Request".equalsIgnoreCase(code)) {
                        reasonCodePredicates.add(criteriaBuilder.isNull(root.get(RESPONSE_REASON_CODE_VALUE)));
                    } else {
                        nonNullCodes.add(code.toLowerCase(Locale.ROOT));
                    }
                }

                if (!nonNullCodes.isEmpty()) {
                    CriteriaBuilder.In<String> inClause = criteriaBuilder
                            .in(criteriaBuilder.lower(root.get(RESPONSE_REASON_CODE_VALUE)));
                    nonNullCodes.forEach(inClause::value);
                    reasonCodePredicates.add(inClause);
                }

                predicates.add(criteriaBuilder.or(reasonCodePredicates.toArray(new Predicate[predicates.size()])));
            }

            if (idType != null && !idType.isEmpty()) {
                List<Predicate> orPredicates = new ArrayList<>();
                for (String type : idType) {
                    if ("name".equalsIgnoreCase(type)) {
                        orPredicates.add(criteriaBuilder.like(root.get(VERIFICATION_TYPE), "%NAME%"));
                    } else {
                        orPredicates.add(criteriaBuilder.equal(root.get(ID_TYPE), type));
                    }
                }
                if (!orPredicates.isEmpty()) {
                    predicates.add(criteriaBuilder.or(orPredicates.toArray(new Predicate[0])));
                }
            }

            if (!StringUtils.isEmpty(idValue)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(ID_VALUE)),
                        idValue.toLowerCase(Locale.ROOT)));
            }

            if (requestedOrgNames != null && !requestedOrgNames.isEmpty()) {
                CriteriaBuilder.In<String> inClause = criteriaBuilder
                        .in(criteriaBuilder.lower(root.get(REQUESTED_ORG_NAME_VALUE)));
                for (String orgName : requestedOrgNames) {
                    inClause.value(orgName.toLowerCase(Locale.ROOT));
                }
                predicates.add(inClause);
            }

            if (!StringUtils.isEmpty(accountName)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(ACCOUNT_NAME)),
                        accountName.toLowerCase(Locale.ROOT)));
            }

            if (!StringUtils.isEmpty(accountId)) {
                predicates.add(criteriaBuilder.and(
                        criteriaBuilder.or(criteriaBuilder.equal(root.get(IDENTIFICATION_TYPE), "VAT"),
                                criteriaBuilder.equal(root.get(IDENTIFICATION_TYPE), "TIN")),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get(IDENTIFICATION_VALUE)),
                                accountId.toLowerCase(Locale.ROOT))));
            }

            if (!StringUtils.isEmpty(userId)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get(USER_ID_VALUE)),
                        userId.toLowerCase(Locale.ROOT)));
            }

            if (!StringUtils.isEmpty(clientId)) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(CLIENT_ID_VALUE)),
                        clientId.toLowerCase(Locale.ROOT)));
            }

            if (fromTime != null && toTime != null) {
                predicates.add(criteriaBuilder.between(root.get(DEFAULT_SORT_VALUE), fromTime, toTime));
            } else if (fromTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(DEFAULT_SORT_VALUE), fromTime));
            } else if (toTime != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(DEFAULT_SORT_VALUE), toTime));
            }

            if (!StringUtils.isEmpty(transactionId)) {
                predicates.add(criteriaBuilder.equal(root.get(TRANSACTION_ID), transactionId));
            }

            if (!StringUtils.isEmpty(requestSource)) {
                predicates.add(criteriaBuilder.equal(root.get(REQUEST_SOURCE), requestSource));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        });


        StopWatch sw = new StopWatch();
        sw.start("repo.findAll(spec,pageable)");

        Page<RequestAudit> pagedContent = requestAuditRepository.findAll(specification, pageable);
        sw.stop();
        long tookMs = sw.getLastTaskTimeMillis();
        log.info("findAll(spec,pageable) took {} ms (pageSize={}, pageNumber={})",
                 tookMs, pageable.getPageSize(), pageable.getPageNumber());

        StopWatch sw2 = new StopWatch();
        sw2.start("AuditDto");
        List<AuditDto> auditDto = pagedContent.getContent().stream()
                .map(AuditDto::new)
                .collect(Collectors.toList());
        sw2.stop();
        long tookMs2 = sw2.getLastTaskTimeMillis();
        log.info("mapping to AuditDto took {} ms (pageSize={}, pageNumber={})",
                tookMs2, pageable.getPageSize(), pageable.getPageNumber());

        return new PageImpl<>(auditDto, pageable, pagedContent.getTotalElements());
    }
}

