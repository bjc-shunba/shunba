package com.baidu.shunba.service;

import com.baidu.shunba.bean.ResponseMessage;
import com.baidu.shunba.bean.message.SecondLineShiftRequestMessage;
import com.baidu.shunba.entity.SBShift;
import com.baidu.shunba.exceptions.AppException;

import java.util.Collection;
import java.util.List;

public interface SBShiftService extends BaseService<SBShift, String> {
    SBShift findByShiftNo(String shiftNo);

    List<SBShift> findAllByShiftNo(String shiftNo);

    List<SBShift> findAllByShiftNo(Collection<String> shiftNos);

    List<SBShift> findAllByLineId(String lineId);

    List<SBShift> findAllBySeq(Integer seq);

    ResponseMessage pullShift(SecondLineShiftRequestMessage lineShiftRequestMessage) throws AppException;
}
