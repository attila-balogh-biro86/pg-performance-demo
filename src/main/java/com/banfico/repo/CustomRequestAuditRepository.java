package com.banfico.repo;

import com.banfico.model.RequestAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CustomRequestAuditRepository extends JpaRepository<RequestAudit, Long> {

    @Query(value = """
      SELECT t
      FROM RequestAudit t
      WHERE (:iban IS NULL OR t.iban = :iban)
        AND (:assigneeBic  IS NULL OR t.assigneeBic  = :assigneeBic)
        AND (:clientId  IS NULL OR t.clientId  = :clientId)
        AND (:userId  IS NULL OR t.userId  = :userId)
        AND (:currency  IS NULL OR t.currency  = :currency)
        AND (:requestedOrgName  IS NULL OR t.requestedOrgName  = :requestedOrgName)
        AND (:amount IS NULL OR t.amount >= :amount)
      """)
    Slice<RequestAudit> search(
            @Param("iban") String iban,
            @Param("assigneeBic") String assigneeBic,
            @Param("requestedOrgName") String requestedOrgName,
            @Param("userId") String userId,
            @Param("clientId") String clientId,
            @Param("amount") BigDecimal amount,
            @Param("currency") String currency,
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(t.id) FROM RequestAudit t
    WHERE (:iban IS NULL OR t.iban = :iban)
        AND (:assigneeBic  IS NULL OR t.assigneeBic  = :assigneeBic)
        AND (:clientId  IS NULL OR t.clientId  = :clientId)
        AND (:userId  IS NULL OR t.userId  = :userId)
        AND (:currency  IS NULL OR t.currency  = :currency)
        AND (:requestedOrgName  IS NULL OR t.requestedOrgName  = :requestedOrgName)
        AND (:amount IS NULL OR t.amount >= :amount)
    """)
    long countByFilters(
            @Param("iban") String iban,
            @Param("assigneeBic") String assigneeBic,
            @Param("requestedOrgName") String requestedOrgName,
            @Param("userId") String userId,
            @Param("clientId") String clientId,
            @Param("amount") BigDecimal amount,
            @Param("currency") String currency
    );

}

