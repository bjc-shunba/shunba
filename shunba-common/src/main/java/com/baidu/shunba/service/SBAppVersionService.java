package com.baidu.shunba.service;

import com.baidu.shunba.entity.SBAppVersion;
import com.baidu.shunba.exceptions.HasNoneUnusedVersionException;

import java.util.List;

public interface SBAppVersionService extends BaseService<SBAppVersion, String> {
    SBAppVersion getLatestVersion();

    List<SBAppVersion> findAll();

    SBAppVersion findByVersion(String version);

    SBAppVersion saveOrUpdate(SBAppVersion version);

    void useNewVersion() throws HasNoneUnusedVersionException;
}
