package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.QueryCriteria;
import com.baidu.shunba.dao.SBAppVersionRepository;
import com.baidu.shunba.entity.SBAppVersion;
import com.baidu.shunba.exceptions.HasNoneUnusedVersionException;
import com.baidu.shunba.service.SBAppVersionService;
import com.baidu.shunba.utils.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SBAppVersionServiceImpl extends BaseServiceImpl<SBAppVersion, String> implements SBAppVersionService {
    @Autowired
    private SBAppVersionRepository sbAppVersionRepository;

    private Sort getSort() {
        return Sort.by(new Sort.Order(Sort.Direction.DESC, "verMain"),
                new Sort.Order(Sort.Direction.DESC, "verSub"),
                new Sort.Order(Sort.Direction.DESC, "verThird"));
    }

    @Override
    public SBAppVersion getLatestVersion() {
        return sbAppVersionRepository.getLatestVersion().orElse(null);
    }

    @Override
    protected JpaRepository<SBAppVersion, String> getJpaRepository() {
        return sbAppVersionRepository;
    }

    @Override
    protected JpaSpecificationExecutor<SBAppVersion> getJpaSpecificationExecutor() {
        return sbAppVersionRepository;
    }

    @Override
    public List<SBAppVersion> findAll() {
        List<SBAppVersion> all = sbAppVersionRepository.findAll(getSort());

        all.forEach(sbAppVersion -> {
            sbAppVersion.setCount(sbAppVersionRepository.findCountPerVersion(sbAppVersion.getVersion()));
        });

        return all;
    }

    @Override
    public Page<SBAppVersion> findByPage(QueryCriteria criteria, Pageable pageable) {
        Page<SBAppVersion> byPage = super.findByPage(criteria, pageable);

        byPage.getContent().forEach(sbAppVersion -> {
            sbAppVersion.setCount(sbAppVersionRepository.findCountPerVersion(sbAppVersion.getVersion()));
        });

        return byPage;
    }

    @Override
    public SBAppVersion findByVersion(String version) {
        if (StringUtils.isBlank(version)) {
            return null;
        }

        List<SBAppVersion> byVersion = sbAppVersionRepository.findByVersion(version);

        return byVersion == null || byVersion.isEmpty() ? null : byVersion.get(0);
    }

    @Override
    public SBAppVersion saveOrUpdate(SBAppVersion version) {
        Date now = new Date();

        SBAppVersion existsVersion = this.findByVersion(version.getVersion());

        SBAppVersion forSave = null;
        if (existsVersion == null) {
            forSave = version;
            forSave.setCreateDate(now);
            forSave.setUpdateDate(now);

            String[] split = version.getVersion().split("\\.");

            if (split.length > 0) {
                forSave.setVerMain(ObjectUtils.getInteger(split[0]));
            }
            if (split.length > 1) {
                forSave.setVerSub(ObjectUtils.getInteger(split[1]));
            }
            if (split.length > 2) {
                forSave.setVerThird(ObjectUtils.getInteger(split[2]));
            }
        } else {
            forSave = existsVersion;
            forSave.setUpdateDate(now);
            forSave.setMemo(version.getMemo());
            // 清空使用时间
            forSave.setUseDate(null);
        }

        return sbAppVersionRepository.saveAndFlush(forSave);
    }

    @Override
    public void useNewVersion() throws HasNoneUnusedVersionException {
        SBAppVersion sbAppVersion = sbAppVersionRepository.findNewVersion().orElse(null);

        if (sbAppVersion == null) {
            throw new HasNoneUnusedVersionException();
        }

        Date now = new Date();
        // sbAppVersion.setUpdateDate(now);
        sbAppVersion.setUseDate(now);

        sbAppVersionRepository.saveAndFlush(sbAppVersion);
    }
}
