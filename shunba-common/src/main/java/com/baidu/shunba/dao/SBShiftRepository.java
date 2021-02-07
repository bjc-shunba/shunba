package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SBShift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SBShiftRepository extends JpaRepository<SBShift, String>, JpaSpecificationExecutor<SBShift> {
    Optional<SBShift> findFirstByShiftNo(String shiftNo);

    List<SBShift> findAllByLineId(String lineId);

    List<SBShift> findAllBySeq(Integer seq);

    List<SBShift> findAllByShiftNo(String shiftNo);

    List<SBShift> findAllByShiftNoIn(Collection<String> shiftNo);
}
