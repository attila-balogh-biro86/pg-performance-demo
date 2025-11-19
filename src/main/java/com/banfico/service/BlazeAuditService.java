package com.banfico.service;

import com.banfico.dto.KeysetPageResponse;
import com.banfico.dto.KeysetPageToken;
import com.banfico.model.RequestAudit;
import com.banfico.model.TransactionFilter;
import com.banfico.repo.RequestAuditBlazePredicates;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
import com.blazebit.persistence.spring.data.repository.KeysetPageRequest;
import com.blazebit.persistence.spring.data.repository.KeysetPageable;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BlazeAuditService {

    @Autowired
    private final CriteriaBuilderFactory cbf;

    @PersistenceContext
    private final EntityManager em;

    public BlazeAuditService(CriteriaBuilderFactory cbf, EntityManager em) {
        this.cbf = cbf;
        this.em = em;
    }

    public PagedList<RequestAudit> search(TransactionFilter filter,
                                                   KeysetPage actualKeySetPage, Integer page, Integer size) {

        return RequestAuditBlazePredicates
                .withFilters(em, cbf, filter)
                .orderByAsc("ra.id")
                .orderByDesc("ra.requestTimeReceived")
                .page(actualKeySetPage, page, size)
                .withKeysetExtraction(true)
                .getResultList();
    }
}

