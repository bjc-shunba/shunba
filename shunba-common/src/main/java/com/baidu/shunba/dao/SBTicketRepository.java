package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SBTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Repository
public interface SBTicketRepository extends JpaRepository<SBTicket, String>, JpaSpecificationExecutor<SBTicket> {
    @Query(value = "SELECT ticket FROM SBTicket ticket WHERE ticket.lineId = ?1 AND (ticket.delFlag IS NULL OR ticket.delFlag <> 1)")
    List<SBTicket> findAllByLineId(String lineId);

    @Query(value = "SELECT ticket FROM SBTicket ticket WHERE ticket.seq = ?1 AND (ticket.delFlag IS NULL OR ticket.delFlag <> 1)")
    List<SBTicket> findAllBySeq(Integer seq);

    @Query(value = "SELECT ticket FROM SBTicket ticket WHERE ticket.ticketNo = ?1 AND (ticket.delFlag IS NULL OR ticket.delFlag <> 1)")
    List<SBTicket> findAllByTicketNo(String ticketId);

    /**
     * 根据票据id, 查找所有未删除的票据
     *
     * @param ticketNos
     * @return
     */
    @Query(value = "SELECT ticket FROM SBTicket ticket WHERE ticket.ticketNo IN ?1 AND (ticket.delFlag IS NULL OR ticket.delFlag <> 1)")
    List<SBTicket> findAllByTicketNoIn(Collection<String> ticketNos);

    /**
     * 根据票据id, 查找所有票据, 包括已删除的票据
     *
     * @param ticketNos
     * @return
     */
    @Query(value = "SELECT ticket FROM SBTicket ticket WHERE ticket.ticketNo IN ?1")
    List<SBTicket> findAllByTicketNo(Collection<String> ticketNos);

    /**
     * 删除退订的票据
     *
     * @param ticketNos
     * @return
     */
    @Transactional
    @Modifying
    @Query(value = "UPDATE sb_ticket SET del_flag = 1 WHERE ticket_no IN ?1", nativeQuery = true)
    int deleteTickets(Collection<String> ticketNos);
}
