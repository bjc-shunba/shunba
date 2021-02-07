package com.baidu.shunba.bean;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 分页信息
 */
@Data
public class PageVo {
    private Integer page;

    private Integer size;

    private Map<String, String> sort;

    public PageVo() {
    }

    public PageVo(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public PageVo(Integer page, Integer size, Map<String, String> sort) {
        this.page = page;
        this.size = size;
        this.sort = sort;
    }

    public Sort getSort() {
        if (sort == null || sort.isEmpty()) {
            return null;
        }

        List<Sort.Order> orders = new ArrayList<>();

        sort.forEach((key, value) -> {
            if (StringUtils.equalsAnyIgnoreCase(value, Sort.Direction.DESC.name())) {
                orders.add(Sort.Order.desc(key));
            } else if (StringUtils.equalsAnyIgnoreCase(value, Sort.Direction.ASC.name())) {
                orders.add(Sort.Order.asc(key));
            }
        });

        return Sort.by(orders);
    }

    public Pageable toPageable() {
        Pageable pageable;

        Sort sort = getSort();

        if (sort == null) {
            pageable = PageRequest.of(this.page, this.size);
        } else {
            pageable = PageRequest.of(this.page, this.size, sort);
        }

        return pageable;
    }

    /**
     * 判断对象是否不包含分页条件
     *
     * @return 不包含分页条件, 返回true; 否则返回false
     */
    public boolean isEmpty() {
        return this.page == null || this.size == null;
    }
}
