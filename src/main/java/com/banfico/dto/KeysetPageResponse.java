package com.banfico.dto;

import java.util.List;

public class KeysetPageResponse<T> {

    private final List<T> content;
    private final long totalElements;
    private final String lowestKeyset;
    private final String highestKeyset;

    public KeysetPageResponse(List<T> content,
                              long totalElements,
                              String lowestKeyset,
                              String highestKeyset) {
        this.content = content;
        this.totalElements = totalElements;
        this.lowestKeyset = lowestKeyset;
        this.highestKeyset = highestKeyset;
    }

    public List<T> getContent() { return content; }
    public long getTotalElements() { return totalElements; }
    public String getLowestKeyset() { return lowestKeyset; }
    public String getHighestKeyset() { return highestKeyset; }
}

