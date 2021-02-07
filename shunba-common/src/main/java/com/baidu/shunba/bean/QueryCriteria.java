package com.baidu.shunba.bean;

import com.baidu.shunba.filter.Filter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Predicate;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用查询条件定义
 */
@Slf4j
public abstract class QueryCriteria {
    /**
     * 查询条件中的查询字段列表
     */
    @JsonIgnore
    private List<Field> declaredFilters;

    private synchronized List<Field> getDeclaredFilters() {
        if (null != declaredFilters) {
            return declaredFilters;
        }

        declaredFilters = new ArrayList<>();

        Field[] allFields = FieldUtils.getAllFields(this.getClass());
        for (Field field : allFields) {
            if (Filter.class.isAssignableFrom(field.getType())) {
                declaredFilters.add(field);
            }
        }

        return declaredFilters;
    }

    /**
     * 使用查询条件, 封装sql条件
     *
     * @param <T>
     * @return Specification查询条件
     */
    public <T> Specification<T> getSpecification() {
        List<Field> declaredFilters = getDeclaredFilters();

        Specification<T> specification = (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = new ArrayList<>();

            this.declaredFilters.forEach(field -> {
                String fieldName = field.getName();
                Filter<?> filter = null;
                try {
                    // PropertyDescriptor dp = new PropertyDescriptor(fieldName, this.getClass());
                    /// filter = (Filter<?>) dp.getReadMethod().invoke(this);
                    filter = (Filter<?>) PropertyUtils.getProperty(this, fieldName);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.error("读取字段{}的条件配置错误!", fieldName);
                    log.error("{}", e);
                }

                if (null == filter) {
                    return;
                }

                Predicate predicate = filter.buildPredicate(fieldName, root, criteriaBuilder);

                if (null != predicate) {
                    list.add(criteriaBuilder.and(predicate));
                }
            });

            Predicate[] predicates = new Predicate[list.size()];

            return criteriaBuilder.and(list.toArray(predicates));
        };

        return specification;
    }

    public <T, E extends Filter<T>> E getFilter(String targetFilterName) {
        for (Field declaredFilter : getDeclaredFilters()) {
            String fieldName = declaredFilter.getName();

            if (fieldName.equals(targetFilterName)) {
                try {
                    return (E) PropertyUtils.getProperty(this, fieldName);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.warn("读取查询条件[{}]中的字段[{}]的值错误, 错误原因: {}", "deleteFlag", this.getClass().getName(), e);
                    return null;
                }
            }
        }

        return null;
    }

    public <T> void setFilter(Filter<T> filter, String targetFilterName) {
        for (Field declaredFilter : getDeclaredFilters()) {
            String fieldName = declaredFilter.getName();

            if (fieldName.equals(targetFilterName)) {
                try {
                    PropertyUtils.setProperty(this, fieldName, filter);
                } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                    log.warn("设置查询条件[{}]中的字段[{}]的值错误, 错误原因: {}", targetFilterName, this.getClass().getName(), e);
                }
            }
        }
    }
}
