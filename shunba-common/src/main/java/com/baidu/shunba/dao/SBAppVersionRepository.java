package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SBAppVersion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SBAppVersionRepository extends JpaRepository<SBAppVersion, String>, JpaSpecificationExecutor<SBAppVersion> {
    @Query(value = "SELECT * FROM sb_app_version WHERE use_date is not null and (del_flag IS NULL OR del_flag = 0) ORDER BY ver_main DESC, ver_sub DESC, ver_third DESC LIMIT 1", nativeQuery = true)
    Optional<SBAppVersion> getLatestVersion();

    @Query(value = "SELECT * FROM sb_app_version WHERE use_date is null and (del_flag IS NULL OR del_flag = 0) ORDER BY update_date ASC LIMIT 1", nativeQuery = true)
    Optional<SBAppVersion> findNewVersion();

    @Query("select v FROM SBAppVersion v WHERE v.version = ?1 and (v.delFlag is null OR v.delFlag <> 1)")
    List<SBAppVersion> findByVersion(String version);

    @Query(value = "SELECT COUNT(d.id) as count FROM SBAppVersion v LEFT JOIN SBDevice d ON v.version = d.appVersion WHERE v.version = ?1 GROUP BY v.version")
    int findCountPerVersion(String version);
}
