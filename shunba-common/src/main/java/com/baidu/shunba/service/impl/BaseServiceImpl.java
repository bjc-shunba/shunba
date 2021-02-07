package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.PageVo;
import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.bean.QueryVo;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.filter.impl.IntegerFilter;
import com.baidu.shunba.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {
    protected abstract JpaRepository<T, ID> getJpaRepository();

    protected abstract JpaSpecificationExecutor<T> getJpaSpecificationExecutor();

    private static final String DEL_FLAG_FILTER_NAME = "delFlag";

    @Override
    public Optional<T> findById(ID id) {
        return getJpaRepository().findById(id);
    }

    @Override
    public Optional<T> findOne(QueryCriteria criteria) {
        PageVo page = new PageVo(0, 1);

        Page<T> byPage = this.findByPage(criteria, page.toPageable());

        if (byPage.getTotalElements() > 0) {
            return Optional.of(byPage.getContent().get(0));
        }

        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        return getJpaRepository().findAll();
    }

    @Override
    public List<T> findAll(QueryCriteria criteria) {
        if (criteria == null) {
            return this.findAll();
        }

        return getJpaSpecificationExecutor().findAll(criteria.getSpecification());
    }

    @Override
    public List<T> findAll(Sort sort) {
        return getJpaRepository().findAll(sort);
    }

    @Override
    public List<T> findAll(QueryCriteria criteria, Sort sort) {
        return getJpaSpecificationExecutor().findAll(criteria.getSpecification(), sort);
    }

    @Override
    public Page<T> findByPage(Pageable pageable) {
        return getJpaRepository().findAll(pageable);
    }

    @Override
    public Page<T> findByPage(QueryCriteria criteria, Pageable pageable) {
        if (criteria == null) {
            return this.findByPage(pageable);
        }

        return getJpaSpecificationExecutor().findAll(criteria.getSpecification(), pageable);
    }

    @Override
    public <E extends QueryCriteria> Object commonQuery(QueryVo<E> query) {
        if (null == query) {
            return this.findAll();
        }

        PageVo page = query.getPage();
        E queryCriteria = query.getQuery();

        if (null != page && !page.isEmpty()) {
            return this.findByPage(queryCriteria, page.toPageable());
        }

        Sort sort = null == page ? null : page.getSort();

        if (sort == null) {
            return this.findAll(queryCriteria);
        }

        return this.findAll(queryCriteria, sort);
    }

    @Override
    public <E extends QueryCriteria> Object commonQuery(QueryVo<E> query, Class<E> queryCriteriaClass, boolean isSearchDeleted) throws IllegalAccessException, InstantiationException {
        if (isSearchDeleted) {
            return this.commonQuery(query);
        }

        E queryQuery = query.getQuery();

        if (queryQuery == null) {
            try {
                queryQuery = queryCriteriaClass.newInstance();
                query.setQuery(queryQuery);
            } catch (InstantiationException | IllegalAccessException e) {
                log.error("初始化查询条件: [{}] 错误, 错误原因: {}", queryCriteriaClass.getName(), e);
                throw e;
            }
        }

        IntegerFilter deleteFlag = queryQuery.getFilter(DEL_FLAG_FILTER_NAME);

        if (deleteFlag == null) {
            deleteFlag = new IntegerFilter();
            deleteFlag.setEquals(0);
            queryQuery.setFilter(deleteFlag, DEL_FLAG_FILTER_NAME);
        } else if (deleteFlag.isEmpty()) {
            deleteFlag.setEquals(0);
        }

        return this.commonQuery(query);
    }

    @Override
    public T save(T entity) throws AppException {
        return getJpaRepository().save(entity);
    }

    @Override
    public T saveAndFlush(T entity) throws AppException {
        return getJpaRepository().saveAndFlush(entity);
    }

    @Override
    public List<T> saveAll(Iterable<T> var1) throws AppException {
        return getJpaRepository().saveAll(var1);
    }

    @Override
    public void delete(T entity) throws AppException {
        getJpaRepository().delete(entity);
    }

    @Override
    public void deleteById(ID id) throws AppException {
        getJpaRepository().deleteById(id);
    }

    @Override
    public void deleteInBatch(Iterable<T> var1) throws AppException {
        getJpaRepository().deleteInBatch(var1);
    }
}
