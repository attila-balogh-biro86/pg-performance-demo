package com.banfico.repo;

import com.banfico.model.RequestAudit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RequestAuditRepository
        extends JpaRepository<RequestAudit, String> {

    Page<RequestAudit> findAll(Specification<RequestAudit> specification, Pageable pageable);
}

