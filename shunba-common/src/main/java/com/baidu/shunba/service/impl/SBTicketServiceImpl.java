package com.baidu.shunba.service.impl;

import com.baidu.shunba.bean.message.TicketRequestMessage;
import com.baidu.shunba.dao.SBTicketRepository;
import com.baidu.shunba.entity.SBDevice;
import com.baidu.shunba.entity.SBShift;
import com.baidu.shunba.entity.SBTicket;
import com.baidu.shunba.exceptions.AppException;
import com.baidu.shunba.service.SBDeviceService;
import com.baidu.shunba.service.SBShiftService;
import com.baidu.shunba.service.SBTicketService;
import com.baidu.shunba.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SBTicketServiceImpl extends BaseServiceImpl<SBTicket, String> implements SBTicketService {
    @Autowired
    private SBTicketRepository sbTicketRepository;

    @Autowired
    private SBDeviceService sbDeviceService;

    @Autowired
    private SBShiftService sbShiftService;

    @Override
    protected JpaRepository<SBTicket, String> getJpaRepository() {
        return sbTicketRepository;
    }

    @Override
    protected JpaSpecificationExecutor<SBTicket> getJpaSpecificationExecutor() {
        return sbTicketRepository;
    }

    @Override
    public List<SBTicket> findAllByLineId(String lineId) {
        return sbTicketRepository.findAllByLineId(lineId);
    }

    @Override
    public List<SBTicket> findAllBySeq(Integer seq) {
        return sbTicketRepository.findAllBySeq(seq);
    }

    @Override
    public List<SBTicket> findAllByTicketNo(String ticketId) {
        return sbTicketRepository.findAllByTicketNo(ticketId);
    }

    @Override
    public List<SBTicket> saveTicketResult(List<SBTicket> tickets) throws AppException {
        if (null == tickets || tickets.isEmpty()) {
            return new ArrayList<>();
        }

        String deviceId = tickets.get(0).getDeviceId();

        SBDevice device = sbDeviceService.findByDeviceId(deviceId).orElse(null);

        if (device == null) {
            throw new AppException("未找到设备: " + deviceId);
        }

        sbDeviceService.updateLastPostTicketTime(deviceId);

        Map<String, SBTicket> ticketMap = tickets.stream().collect(Collectors.toMap(SBTicket::getTicketNo, sbTicket -> sbTicket));

        List<SBTicket> allExistsTickets = sbTicketRepository.findAllByTicketNoIn(ticketMap.keySet());
        // 所有待更新到库中的ticket
        List<SBTicket> ticketsForSave = new ArrayList<>();
        List<SBTicket> ticketForSync = new ArrayList<>();

        ticketMap.forEach(((ticketNo, newTicket) -> {
            SBTicket sbTicketOld = allExistsTickets.stream().filter(ticket -> StringUtils.equals(ticket.getTicketNo(), ticketNo)).findFirst().orElse(null);
            if (sbTicketOld == null) {
                newTicket.setSyncFlag(1);
                if (StringUtils.isBlank(newTicket.getShiftId())) {
                    newTicket.setShiftId(newTicket.getShiftNo());
                } else if (StringUtils.isBlank(newTicket.getShiftNo())) {
                    newTicket.setShiftNo(newTicket.getShiftId());
                }
                if (StringUtils.isBlank(newTicket.getTicketId())) {
                    newTicket.setTicketId(newTicket.getTicketNo());
                } else if (StringUtils.isBlank(newTicket.getTicketNo())) {
                    newTicket.setTicketNo(newTicket.getTicketId());
                }
                ticketsForSave.add(newTicket);
                return;
            }

            if (ObjectUtils.sameWidth(sbTicketOld.getIsCheck(), 1) && !ObjectUtils.sameWidth(newTicket.getIsCheck(), 1)) {
                // 数据尚未同步, 添加到待同步列表
                if (sbTicketOld.getSyncFlag() != 2) {
                    ticketForSync.add(sbTicketOld);
                }
                return;
            }

            sbTicketOld.setSyncFlag(1);
            sbTicketOld.setIsCheck(newTicket.getIsCheck());
            sbTicketOld.setCheckTime(newTicket.getCheckTime());
            sbTicketOld.setCheckType(newTicket.getCheckType());
            sbTicketOld.setTemperature(newTicket.getTemperature());
            sbTicketOld.setDeviceId(newTicket.getDeviceId());
            ticketsForSave.add(sbTicketOld);
        }));

        ticketForSync.addAll(sbTicketRepository.saveAll(ticketsForSave));

        return ticketForSync;
    }

    @Override
    public Collection<SBTicket> receiveTickets(TicketRequestMessage message) throws AppException {
        Set<String> shiftIds = new HashSet<>();
        Set<String> ticketIds = new HashSet<>();

        // 待退票的票据
        Set<String> ticketsForDelete = new HashSet<>();
        // 待更新的票据
        Map<String, SBTicket> ticketMap = new HashMap<>();

        message.getTicketAry().forEach(secondTicket -> {
            String ticketNo = secondTicket.getTicketNo();

            if (ObjectUtils.sameWidth(secondTicket.getOperate(), 1)) {
                // 退票
                ticketsForDelete.add(ticketNo);
                return;
            }

            String shiftNo = secondTicket.getShiftNo();

            SBTicket ticket = new SBTicket();

            shiftIds.add(shiftNo);
            ticketIds.add(ticketNo);

            ticket.setSyncFlag(0);

            ticket.setTicketNo(ticketNo);
            ticket.setTicketId(ticketNo);

            ticket.setMemberId(secondTicket.getMemberId());

            ticket.setLineId(secondTicket.getLineId());

            ticket.setShiftNo(shiftNo);
            ticket.setShiftId(shiftNo);

            ticket.setDelFlag(0);

            ticketMap.put(ticketNo, ticket);
        });

        List<SBTicket> allExistsTickets = ticketIds.size() > 0 ? sbTicketRepository.findAllByTicketNo(ticketIds) : new ArrayList<>();
        List<SBShift> shifts = sbShiftService.findAllByShiftNo(shiftIds);

        Date now = new Date();

        for (String ticketNo : ticketMap.keySet()) {
            SBTicket newTicket = ticketMap.get(ticketNo);

            SBTicket sbTicketExists = allExistsTickets.stream().filter(ticket -> StringUtils.equals(ticket.getTicketNo(), ticketNo)).findFirst().orElse(null);
            SBShift sbShift = shifts.stream().filter(shift -> StringUtils.equals(newTicket.getShiftId(), shift.getShiftId())).findFirst().orElse(null);

            Integer seq = sbShift == null ? null : sbShift.getSeq();

            if (sbTicketExists != null) {
                // 票据已存在
                // if (ObjectUtils.sameWidth(sbTicketExists.getIsCheck(), 1)) {
                // 票据已核销
                // }

                sbTicketExists.setUpdateDate(now);
                sbTicketExists.setMemberId(newTicket.getMemberId());

                sbTicketExists.setLineId(newTicket.getLineId());

                sbTicketExists.setShiftNo(newTicket.getShiftNo());
                sbTicketExists.setShiftId(newTicket.getShiftId());

                sbTicketExists.setSeq(seq);

                sbTicketExists.setDelFlag(0);

                ticketMap.put(ticketNo, sbTicketExists);
            } else {
                newTicket.setCreateDate(now);
                newTicket.setUpdateDate(now);

                newTicket.setSeq(seq);
            }
        }

        List<SBTicket> tickets;
        if (ticketsForDelete.size() > 0) {
            log.info(" ========== 执行删除票据订单: {}", ticketsForDelete);
            sbTicketRepository.deleteTickets(ticketsForDelete);
            tickets = sbTicketRepository.findAllByTicketNo(ticketsForDelete);
        } else {
            tickets = new ArrayList<>();
        }
        if (ticketMap.size() > 0) {
            tickets.addAll(sbTicketRepository.saveAll(ticketMap.values()));
        }

        return tickets;
    }
}
