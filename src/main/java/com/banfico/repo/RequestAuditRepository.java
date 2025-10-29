package com.banfico.repo;

import com.banfico.model.RequestAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestAuditRepository
        extends JpaRepository<RequestAudit, String>, JpaSpecificationExecutor<RequestAudit> {}

