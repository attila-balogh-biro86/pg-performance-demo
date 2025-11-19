package com.banfico.service;

import com.banfico.dto.AuditDto;
import com.banfico.model.RequestAudit;
import com.banfico.model.TransactionFilter;
import com.banfico.repo.RequestAuditRepository;
import com.banfico.repo.RequestAuditSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DefaultJpaAuditService {

    private final RequestAuditRepository repo;

    public DefaultJpaAuditService(RequestAuditRepository repo) {
        this.repo = repo;
    }

    public Page<AuditDto> search(TransactionFilter f, Pageable pageable) {

        Page<RequestAudit> dbPage = repo.findAll(
               RequestAuditSpecs.withFilters(f),
                pageable
        );

        List<AuditDto> auditDto = dbPage.getContent().stream()
                .map(AuditDto::new)
                .toList();

        return new PageImpl<>(
                auditDto,
                dbPage.getPageable(),       // keep pagination
                dbPage.getTotalElements()   // keep total count
        );
    }
}

