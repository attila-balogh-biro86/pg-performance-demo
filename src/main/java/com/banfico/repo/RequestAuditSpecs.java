package com.banfico.repo;

import com.banfico.model.RequestAudit;
import com.banfico.model.TransactionFilter;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class RequestAuditSpecs {

    public static Specification<RequestAudit> withFilters(TransactionFilter filter) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (filter.assigneeBic() != null) {
                predicates.add(cb.equal(root.get("assigneeBic"), filter.assigneeBic()));
            }
            if (filter.iban() != null) {
                predicates.add(cb.equal(root.get("iban"), filter.iban()));
            }
            if (filter.amount() != null) {
                predicates.add(cb.ge(root.get("amount"), filter.amount()));
            }
            if (filter.requestedOrgName() != null) {
                predicates.add(cb.equal(root.get("requestedOrgName"), filter.requestedOrgName()));
            }
            if (filter.currency() != null) {
                predicates.add(cb.equal(root.get("currency"), filter.currency()));
            }
            if (filter.clientId() != null) {
                predicates.add(cb.equal(root.get("clientId"), filter.clientId()));
            }
            if (filter.userId() != null) {
                predicates.add(cb.equal(root.get("userId"), filter.userId()));
            }
            if (filter.requestTimeReceivedStartFrom()!= null && filter.requestTimeReceivedStartTo() != null) {
                predicates.add(cb.between(root.get("requestTimeReceived"), filter.requestTimeReceivedStartFrom(), filter.requestTimeReceivedStartTo()));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}

