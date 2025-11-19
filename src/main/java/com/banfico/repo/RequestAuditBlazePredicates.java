package com.banfico.repo;

import com.banfico.model.RequestAudit;
import com.banfico.model.TransactionFilter;
import com.blazebit.persistence.CriteriaBuilder;
import com.blazebit.persistence.CriteriaBuilderFactory;
import jakarta.persistence.EntityManager;

public class RequestAuditBlazePredicates {

    public static CriteriaBuilder<RequestAudit> withFilters(EntityManager em,
            CriteriaBuilderFactory cbf, TransactionFilter filter) {

        CriteriaBuilder<RequestAudit> cb = cbf.create(em,RequestAudit.class, "ra");

        if (filter.assigneeBic() != null) {
            cb = cb.where("ra.assigneeBic").eq(filter.assigneeBic());
        }
        if (filter.iban() != null) {
            cb = cb.where("ra.iban").eq(filter.iban());
        }
        if (filter.amount() != null) {
            cb = cb.where("ra.amount").ge(filter.amount());
        }
        if (filter.requestedOrgName() != null) {
            cb = cb.where("ra.requestedOrgName").eq(filter.requestedOrgName());
        }
        if (filter.currency() != null) {
            cb = cb.where("ra.currency").eq(filter.currency());
        }
        if (filter.clientId() != null) {
            cb = cb.where("ra.clientId").eq(filter.clientId());
        }
        if (filter.userId() != null) {
            cb = cb.where("ra.userId").eq(filter.userId());
        }
        if (filter.requestTimeReceivedStartFrom() != null &&
                filter.requestTimeReceivedStartTo() != null) {
            cb = cb.where("ra.requestTimeReceived")
                    .between(filter.requestTimeReceivedStartFrom())
                    .and(filter.requestTimeReceivedStartTo());
        }

        return cb;
    }
}
