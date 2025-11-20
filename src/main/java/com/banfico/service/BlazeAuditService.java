package com.banfico.service;

import com.banfico.model.RequestAudit;
import com.banfico.model.TransactionFilter;
import com.banfico.repo.RequestAuditBlazePredicates;
import com.blazebit.persistence.CriteriaBuilderFactory;
import com.blazebit.persistence.KeysetPage;
import com.blazebit.persistence.PagedList;
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
                                          Integer page, Integer size, KeysetPage keysetPage) {

        return RequestAuditBlazePredicates
                .withFilters(em, cbf, filter)
                .orderByAsc("ra.id")
                .orderByDesc("ra.requestTimeReceived")
                .page(keysetPage, page, size)
                .withKeysetExtraction(true)
                .getResultList();
    }
}

