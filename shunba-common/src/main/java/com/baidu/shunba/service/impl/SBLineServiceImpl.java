package com.baidu.shunba.service.impl;

import com.baidu.shunba.dao.SBLineRepository;
import com.baidu.shunba.entity.SBLine;
import com.baidu.shunba.service.SBLineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class SBLineServiceImpl extends BaseServiceImpl<SBLine, String> implements SBLineService {
    @Autowired
    private SBLineRepository sbLineRepository;

    @Override
    protected JpaRepository<SBLine, String> getJpaRepository() {
        return sbLineRepository;
    }

    @Override
    protected JpaSpecificationExecutor<SBLine> getJpaSpecificationExecutor() {
        return sbLineRepository;
    }

    @Override
    public Optional<SBLine> findByLineId(String lineId) {
        return sbLineRepository.findFirstByLineId(lineId);
    }

    @Override
    public List<SBLine> findAllByLineId(String lineId) {
        return sbLineRepository.findAllByLineId(lineId);
    }

    @Override
    public List<SBLine> findAllByLineId(Collection<String> lineId) {
        if (null == lineId || lineId.isEmpty()) {
            return new ArrayList<>();
        }
        return sbLineRepository.findAllByLineIdIn(lineId);
    }
}
