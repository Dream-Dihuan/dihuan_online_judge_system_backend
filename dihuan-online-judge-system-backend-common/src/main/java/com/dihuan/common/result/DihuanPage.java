package com.dihuan.common.result;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class DihuanPage<T> extends Page<T> {
    @JsonIgnore
    private List<OrderItem> orders;

    @JsonIgnore
    private Boolean optimizeCountSql;

    @JsonIgnore
    private Boolean searchCount;

    @JsonIgnore
    private Long maxLimit;

    @JsonIgnore
    private String countId;


    @JsonIgnore
    @Override
    public long getSize(){
        return super.getSize();
    }

    public DihuanPage(long current, long size) {
        super(current, size, 0L);
    }

    public DihuanPage(long current, long size, long total) {
        super(current, size, total, true);
    }

    public DihuanPage(long current, long size, boolean searchCount) {
        super(current, size, 0L, searchCount);
    }

    public DihuanPage(long current, long size, long total, boolean searchCount) {
        this.records = Collections.emptyList();
        this.total = 0L;
        this.size = 10L;
        this.current = 1L;
        this.orders = new ArrayList<>();
        this.optimizeCountSql = true;
        this.searchCount = true;
        this.optimizeJoinOfCountSql = true;
        if (current > 1L) {
            this.current = current;
        }

        this.size = size;
        this.total = total;
        this.searchCount = searchCount;
    }
}
