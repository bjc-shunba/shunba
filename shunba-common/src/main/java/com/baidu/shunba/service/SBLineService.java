package com.baidu.shunba.service;

import com.baidu.shunba.entity.SBLine;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SBLineService extends BaseService<SBLine, String> {
    Optional<SBLine> findByLineId(String lineId);

    List<SBLine> findAllByLineId(String lineId);

    List<SBLine> findAllByLineId(Collection<String> lineId);
}
