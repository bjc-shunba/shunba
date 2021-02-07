package com.baidu.shunba.service;

import com.baidu.shunba.entity.SbDriverDispatchRecord;

public interface SbDriverDispatchRecordService {
    SbDriverDispatchRecord findSbDriverDispatchRecord(String shiftNo);

    SbDriverDispatchRecord save(SbDriverDispatchRecord record);
}
