package com.baidu.shunba.service.impl;

import com.baidu.shunba.dao.SBDriverClockRecordRepository;
import com.baidu.shunba.entity.SBDriverClockRecord;
import com.baidu.shunba.service.SBDriverClockRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SBDriverClockRecordServiceImpl implements SBDriverClockRecordService {
    @Autowired
    private SBDriverClockRecordRepository recordRepository;

    @Override
    public SBDriverClockRecord save(SBDriverClockRecord record) {
        return recordRepository.saveAndFlush(record);
    }
}
