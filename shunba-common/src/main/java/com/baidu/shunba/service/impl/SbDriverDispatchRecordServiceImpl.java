package com.baidu.shunba.service.impl;

import com.baidu.shunba.dao.SbDriverDispatchRecordRepository;
import com.baidu.shunba.entity.SbDriverDispatchRecord;
import com.baidu.shunba.service.SbDriverDispatchRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SbDriverDispatchRecordServiceImpl implements SbDriverDispatchRecordService {
    @Autowired
    private SbDriverDispatchRecordRepository sbDriverDispatchRecordRepository;

    @Override
    public SbDriverDispatchRecord findSbDriverDispatchRecord(String shiftNo) {
        return sbDriverDispatchRecordRepository.findTopByShiftNo(shiftNo).orElse(null);
    }

    @Override
    public SbDriverDispatchRecord save(SbDriverDispatchRecord record) {
        return sbDriverDispatchRecordRepository.saveAndFlush(record);
    }
}
