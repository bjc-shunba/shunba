package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.ResponseMessage;
import com.baidu.shunba.bean.message.SecondLineShift;
import com.baidu.shunba.bean.message.SecondLineShiftRequestMessage;
import com.baidu.shunba.dao.SBShiftRepository;
import com.baidu.shunba.entity.SBLine;
import com.baidu.shunba.entity.SBShift;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.service.SBLineService;
import com.baidu.shunba.service.SBShiftService;
import com.baidu.shunba.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SBShiftServiceImpl extends BaseServiceImpl<SBShift, String> implements SBShiftService {
    @Autowired
    private SBShiftRepository sbShiftRepository;

    @Autowired
    private SBLineService sbLineService;

    @Override
    protected JpaRepository<SBShift, String> getJpaRepository() {
        return sbShiftRepository;
    }

    @Override
    protected JpaSpecificationExecutor<SBShift> getJpaSpecificationExecutor() {
        return sbShiftRepository;
    }

    @Override
    public SBShift findByShiftNo(String shiftNo) {
        return sbShiftRepository.findFirstByShiftNo(shiftNo).orElse(null);
    }

    @Override
    public List<SBShift> findAllByShiftNo(String shiftNo) {
        return sbShiftRepository.findAllByShiftNo(shiftNo);
    }

    @Override
    public List<SBShift> findAllByShiftNo(Collection<String> shiftNos) {
        if (null == shiftNos || shiftNos.isEmpty()) {
            return new ArrayList<>();
        }
        return sbShiftRepository.findAllByShiftNoIn(shiftNos);
    }

    @Override
    public List<SBShift> findAllByLineId(String lineId) {
        return sbShiftRepository.findAllByLineId(lineId);
    }

    @Override
    public List<SBShift> findAllBySeq(Integer seq) {
        return sbShiftRepository.findAllBySeq(seq);
    }

    @Override
    public ResponseMessage pullShift(SecondLineShiftRequestMessage lineShiftRequestMessage) throws AppException {
        List<SecondLineShift> lineShiftList = lineShiftRequestMessage.getLineAry();

        if (null == lineShiftList || lineShiftList.isEmpty()) {
            return ResponseMessage.validError(lineShiftRequestMessage.getTransSn(), "数据为空");
        }

        Date startDate = lineShiftRequestMessage.getStartDate();
        int startSeq = DateUtils.getYearMonthDaySeq(startDate);
        int seqToday = DateUtils.getYearMonthDaySeq(new Date());

        if (startSeq < seqToday) {
            return ResponseMessage.validError(lineShiftRequestMessage.getTransSn(), "不接收今天之前的班次");
        }

        Set<String> lineIds = new HashSet<>();
        Set<String> shiftNumbers = new HashSet<>();

        // 校验传入的数据, 并从中取得所有待更新的线路以及班次id
        for (SecondLineShift shift : lineShiftList) {
            String lineId = shift.getLineId();
            String shiftNo = shift.getShiftNo();

            if (StringUtils.isBlank(lineId)) {
                return ResponseMessage.validError(lineShiftRequestMessage.getTransSn(), "数据错误, 存在数据未携带lineId");
            }
            if (StringUtils.isBlank(shiftNo)) {
                return ResponseMessage.validError(lineShiftRequestMessage.getTransSn(), "数据错误, 存在数据未携带shiftNo");
            }
            if (StringUtils.isBlank(shift.getDriverId())) {
                return ResponseMessage.validError(lineShiftRequestMessage.getTransSn(), "数据错误, 存在数据未携带driver");
            }

            lineIds.add(lineId);
            shiftNumbers.add(shiftNo);
        }

        // 更新lines
        sbLineService.saveAll(getAllLines(lineIds, startSeq));
        // 更新所有shift
        sbShiftRepository.saveAll(getAllSBShift(shiftNumbers, lineShiftList, startSeq));

        return ResponseMessage.success(lineShiftRequestMessage.getTransSn(), "OK");
    }

    /**
     * 根据传入的lineId, 以及序列号, 获取待更新的路线
     *
     * @param lineIds  待更新的所有线路
     * @param startSeq 发车时间
     * @return 所有待更新的线路. 如果lineIds中的某个line在数据库不存在, 则新建; 否则更新已存在的路线的id
     */
    private List<SBLine> getAllLines(Set<String> lineIds, int startSeq) {
        Date now = new Date();

        List<SBLine> existsLines = sbLineService.findAllByLineId(lineIds);
        List<SBLine> allLines = new ArrayList<>();

        lineIds.forEach(lineId -> {
            SBLine sbLine = null;
            for (SBLine line : existsLines) {
                if (Objects.equals(line.getLineId(), lineId)) {
                    sbLine = line;
                    existsLines.remove(line);
                    break;
                }
            }

            if (sbLine == null) {
                sbLine = new SBLine();
                sbLine.setLineId(lineId);
                sbLine.setCreateDate(now);
            }

            sbLine.setNextSeq(startSeq);
            sbLine.setUpdateDate(now);

            allLines.add(sbLine);
        });

        return allLines;
    }

    /**
     * 根据传入的shiftId, 以及序列号, 获取待更新的班次
     *
     * @param shiftNumbers  所有待更新的班次id
     * @param lineShiftList 新的班次信息数据, 数据将被填充到班次数据中.
     * @param startSeq      班次发车时间序列号
     * @return 待更新的班次. 如果shiftNumbers中的某个班次数据在数据库不存在, 则新建; 否则更新已存在的班次
     */
    private List<SBShift> getAllSBShift(Set<String> shiftNumbers, List<SecondLineShift> lineShiftList, int startSeq) {
        Date now = new Date();

        List<SBShift> existsShifts = this.findAllByShiftNo(shiftNumbers);
        List<SBShift> shifts = new ArrayList<>();

        lineShiftList.forEach(lineShift -> {
            SBShift sbShift = null;

            for (SBShift shift : existsShifts) {
                if (Objects.equals(shift.getShiftId(), lineShift.getShiftNo())) {
                    sbShift = shift;
                    existsShifts.remove(shift);
                    break;
                }
            }

            if (sbShift == null) {
                sbShift = lineShift.setSBShift(new SBShift());
                sbShift.setCreateDate(now);
            } else {
                lineShift.setSBShift(sbShift);
            }

            sbShift.setSeq(startSeq);
            sbShift.setUpdateDate(now);

            shifts.add(sbShift);
        });

        return shifts;
    }
}
