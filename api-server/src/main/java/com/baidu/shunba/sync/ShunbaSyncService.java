package com.baidu.shunba.sync;

import com.baidu.shunba.entity.SBDriverClockRecord;
import com.baidu.shunba.entity.SBTicket;

import java.util.Collection;

/**
 * 同步数据到顺吧服务接口
 */
public interface ShunbaSyncService {
    /**
     * 推送司机打卡记录到顺吧
     *
     * @param sbDriverClockRecord
     */
    void sendDriverClock(SBDriverClockRecord sbDriverClockRecord);

    /**
     * 推送核票结果到顺吧
     *
     * @param sbTickets
     */
    void pushTicket(Collection<SBTicket> sbTickets);
}
