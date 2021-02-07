package com.baidu.shunba.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 范围查询条件字段
 *
 * @param <FIELD_TYPE>
 */
@Data
@EqualsAndHashCode
public abstract class RangeFilter<FIELD_TYPE extends Comparable<? super FIELD_TYPE>> extends Filter<FIELD_TYPE> {
    /**
     * >
     */
    private FIELD_TYPE greaterThan;

    /**
     * <
     */
    private FIELD_TYPE lessThan;

    /**
     * >=
     */
    private FIELD_TYPE greaterOrEqualsThan;

    /**
     * <=
     */
    private FIELD_TYPE lessOrEqualsThan;

    @Override
    public boolean isEmpty() {
        return super.isEmpty() && this.greaterThan == null && this.lessThan == null && this.greaterOrEqualsThan == null && this.lessOrEqualsThan == null;
    }

    @Override
    protected List<Predicate> getOtherPredicates(String fieldName, Root<?> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        final int hasGreaterThan = 1;
        final int hasLessThan = 2;
        final int hasGreaterOrEqualsThan = 4;
        final int hasLessOrEqualsThan = 8;

        int propValue = 0;

        if (null != greaterThan) {
            propValue += hasGreaterThan;
        }
        if (null != lessThan) {
            propValue += hasLessThan;
        }
        if (null != greaterOrEqualsThan) {
            propValue += hasGreaterOrEqualsThan;
        }
        if (null != lessOrEqualsThan) {
            propValue += hasLessOrEqualsThan;
        }

        switch (propValue) {
            case 0:
                // 不带有条件
                break;
            case 1:
                // only greaterThan
                predicates.add(criteriaBuilder.greaterThan(root.get(fieldName), this.greaterThan));
                break;
            case 2:
                // only lessThan
                predicates.add(criteriaBuilder.lessThan(root.get(fieldName), this.lessThan));
                break;
            case 4:
                // only greaterOrEqualsThan
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get(fieldName), this.greaterOrEqualsThan));
                break;
            case 8:
                // only lessOrEqualsThan
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get(fieldName), this.lessOrEqualsThan));
                break;
            default:
                // 范围查询
                FIELD_TYPE greater = null == this.greaterThan ? this.greaterOrEqualsThan : this.greaterThan;
                FIELD_TYPE lese = null == this.lessThan ? this.lessOrEqualsThan : this.lessThan;
                predicates.add(criteriaBuilder.between(root.get(fieldName), greater, lese));
                break;
        }

        return predicates;
    }
}
