package com.baidu.shunba.filter.impl;

import com.baidu.shunba.filter.Filter;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Data
public class StringFilter extends Filter<String> {
    private String startWith;

    private String endWith;

    private String like;

    private String notLike;

    @Override
    public boolean isEmpty() {
        if (!super.isEmpty()) {
            return false;
        }

        return StringUtils.isBlank(this.getEquals())
                && StringUtils.isBlank(this.getNotEquals())
                && StringUtils.isBlank(this.startWith)
                && StringUtils.isBlank(this.endWith)
                && StringUtils.isBlank(this.like)
                && StringUtils.isBlank(this.notLike);
    }

    @Override
    protected List<Predicate> getOtherPredicates(String fieldName, Root<?> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.isNotBlank(this.notLike)) {
            predicates.add(criteriaBuilder.notLike(root.get(fieldName), this.like));
        } else {
            String matchModel = null;
            if (StringUtils.isNotBlank(this.like)) {
                matchModel = "%" + this.like + "%";
            } else if (StringUtils.isNotBlank(this.startWith)) {
                matchModel = this.startWith + "%";
            } else if (StringUtils.isNotBlank(this.endWith)) {
                matchModel = "%" + this.endWith;
            }

            if (StringUtils.isNotBlank(matchModel)) {
                predicates.add(criteriaBuilder.like(root.get(fieldName), matchModel));
            }
        }

        return predicates;
    }
}
