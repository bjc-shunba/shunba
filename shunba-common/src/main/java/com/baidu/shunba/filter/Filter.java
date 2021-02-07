package com.baidu.shunba.filter;

import com.baidu.shunba.filter.impl.*;
import lombok.Data;

import javax.persistence.criteria.*;
import java.util.*;

/**
 * 通用查询条件字段<br>
 * impl包下是所有的类型查询条件字段定义
 *
 * @param <FIELD_TYPE>
 * @see RangeFilter {{范围查询字段定义}}
 * @see com.baidu.shunba.filter.impl.DateFilter {{date}}
 * @see com.baidu.shunba.filter.impl.DoubleFilter {{double}}
 * @see com.baidu.shunba.filter.impl.FloatFilter {{float}}
 * @see com.baidu.shunba.filter.impl.IntegerFilter {{int}}
 * @see com.baidu.shunba.filter.impl.LongFilter {{long}}
 * @see com.baidu.shunba.filter.impl.StringFilter {{String}}
 * @see com.baidu.shunba.filter.impl.BooleanFilter {{Boolean}}
 */
@Data
public abstract class Filter<FIELD_TYPE> {
    /**
     * 等值查询
     */
    private FIELD_TYPE equals;

    /**
     * !=
     */
    private FIELD_TYPE notEquals;

    /**
     * 是否启用非空查询. false时, 查询空值; true时, 开启非空查询.
     */
    private Boolean specification;

    /**
     * in查询
     */
    private Set<FIELD_TYPE> in;

    /**
     * 判断该查询条件是否为空
     *
     * @return
     */
    public boolean isEmpty() {
        return this.equals == null && this.notEquals == null && this.specification == null && (this.in == null || this.in.isEmpty());
    }

    public final Predicate buildPredicate(String fieldName, Root<?> root, CriteriaBuilder criteriaBuilder) {
        List<Predicate> predicates = new ArrayList<>();

        if (null != this.equals) {
            predicates.add(criteriaBuilder.equal(root.get(fieldName), this.equals));
        }

        if (null != this.notEquals) {
            predicates.add(criteriaBuilder.notEqual(root.get(fieldName), this.notEquals));
        }

        if (null != this.in) {
            Path<FIELD_TYPE> objectPath = root.get(fieldName);
            CriteriaBuilder.In<FIELD_TYPE> in = criteriaBuilder.in(objectPath);
            this.in.forEach(value -> in.value(value));
            predicates.add(in);
        }

        predicates.addAll(this.getOtherPredicates(fieldName, root, criteriaBuilder));

        if (null != specification && !this.specification) {
            predicates.add(criteriaBuilder.isNull(root.get(fieldName)));
        }
        if (null != specification && this.specification) {
            predicates.add(criteriaBuilder.isNotNull(root.get(fieldName)));
        }

        if (predicates.size() == 0) {
            return null;
        }

        Predicate[] predicateArray = new Predicate[predicates.size()];

        return criteriaBuilder.and(predicates.toArray(predicateArray));
    }

    protected List<Predicate> getOtherPredicates(String fieldName, Root<?> root, CriteriaBuilder criteriaBuilder) {
        return Collections.emptyList();
    }

    public static StringFilter from(String value) {
        StringFilter filter = new StringFilter();
        filter.setEquals(value);

        return filter;
    }

    public static IntegerFilter from(int value) {
        IntegerFilter filter = new IntegerFilter();
        filter.setEquals(value);

        return filter;
    }

    public static LongFilter from(long value) {
        LongFilter filter = new LongFilter();
        filter.setEquals(value);

        return filter;
    }

    public static FloatFilter from(float value) {
        FloatFilter filter = new FloatFilter();
        filter.setEquals(value);

        return filter;
    }

    public static DoubleFilter from(double value) {
        DoubleFilter filter = new DoubleFilter();
        filter.setEquals(value);

        return filter;
    }

    public static DateFilter from(Date value) {
        DateFilter filter = new DateFilter();
        filter.setEquals(value);

        return filter;
    }

    public static BooleanFilter from(boolean value) {
        BooleanFilter filter = new BooleanFilter();
        filter.setEquals(value);

        return filter;
    }
}
