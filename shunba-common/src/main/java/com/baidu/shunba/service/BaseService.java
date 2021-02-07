package com.baidu.shunba.service;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.bean.QueryVo;
import com.baidu.shunba.exceptions.AppException;
import com.sun.istack.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * 通用查询服务定义
 *
 * @param <T>
 */
public interface BaseService<T, ID> {
    /**
     * 根据id读取数据
     *
     * @param id
     * @return
     */
    Optional<T> findById(ID id);

    Optional<T> findOne(@NotNull QueryCriteria criteria);

    /**
     * 查询所有
     *
     * @return List
     */
    List<T> findAll();

    /**
     * 根据条件查询所有
     *
     * @param criteria QueryCriteria
     * @return List
     */
    List<T> findAll(@NotNull QueryCriteria criteria);

    /**
     * 排序查询所有
     *
     * @param sort 排序依据
     * @return List
     */
    List<T> findAll(@NotNull Sort sort);

    /**
     * 根据查询条件以及排序方式, 查询所有
     *
     * @param criteria QueryCriteria
     * @param sort     排序依据
     * @return List
     */
    List<T> findAll(@NotNull QueryCriteria criteria, @NotNull Sort sort);

    /**
     * 分页查询所有
     *
     * @param pageable QueryCriteria
     * @return Page
     */
    Page<T> findByPage(@NotNull Pageable pageable);

    /**
     * 使用条件分页查询所有
     *
     * @param criteria QueryCriteria
     * @param pageable Pageable
     * @return
     */
    Page<T> findByPage(@NotNull QueryCriteria criteria, @NotNull Pageable pageable);

    /**
     * 通用查询
     *
     * @param query 查询条件
     * @param <E>   QueryCriteria的类型
     * @return 当query中带有PageVo时, 返回分页结果; 否则返回List
     */
    <E extends QueryCriteria> Object commonQuery(QueryVo<E> query);

    /**
     * 通用查询
     *
     * @param query              查询条件
     * @param queryCriteriaClass 查询条件的类型
     * @param isSearchDeleted    是否查询已经被删除的设备, 默认为false不查询. 当请求中带有deleteFlag参数时, 忽略该参数; 当query不包含deleteFlag参数时, 忽略该参数
     * @param <E>                QueryCriteria的类型
     * @return 当query中带有PageVo时, 返回分页结果; 否则返回List
     */
    <E extends QueryCriteria> Object commonQuery(QueryVo<E> query, Class<E> queryCriteriaClass, boolean isSearchDeleted) throws IllegalAccessException, InstantiationException;

    /**
     * 新增或更新实体
     *
     * @param entity entity
     * @return entity
     */
    T save(T entity) throws AppException;

    T saveAndFlush(T entity) throws AppException;

    List<T> saveAll(Iterable<T> var1) throws AppException;

    /**
     * 删除实体
     *
     * @param entity
     */
    void delete(T entity) throws AppException;

    /**
     * 根据id删除实体
     *
     * @param id
     */
    void deleteById(ID id) throws AppException;

    /**
     * 批量删除实体
     *
     * @param var1
     */
    void deleteInBatch(Iterable<T> var1) throws AppException;
}
