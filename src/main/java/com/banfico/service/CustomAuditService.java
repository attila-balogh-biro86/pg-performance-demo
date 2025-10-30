package com.banfico.service;

import com.banfico.cache.FilterHash;
import com.banfico.dto.AuditDto;
import com.banfico.model.RequestAudit;
import com.banfico.model.TransactionFilter;
import com.banfico.repo.RequestAuditRepository;
import com.banfico.repo.RequestAuditSpecs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class CustomAuditService {

    private final RequestAuditRepository repo;
    private final RedisTemplate<String, Long> redis;
    private final FilterHash filterHash;

    public CustomAuditService(RequestAuditRepository repo, RedisTemplate<String, Long> redis, FilterHash filterHash) {
        this.repo = repo;
        this.redis = redis;
        this.filterHash = filterHash;
    }

    public Page<AuditDto> search(TransactionFilter f, Pageable pageable) {

        Slice<RequestAudit> dbPage = repo.findAll(
               RequestAuditSpecs.withFilters(f),
                pageable
        );
        String key = filterHash.keyFor(f);

        Long cachedTotal = redis.opsForValue().get(key);
        long total = (cachedTotal != null) ? cachedTotal : repo.count(RequestAuditSpecs.withFilters(f));
        redis.opsForValue().set(key, total, Duration.ofMinutes(15));

        List<AuditDto> auditDto = dbPage.getContent().stream()
                .map(AuditDto::new)
                .toList();

        // Re-wrap with cached total (so subsequent page navigations reuse it)
        return new PageImpl<>(auditDto, pageable, total);
    }
}

