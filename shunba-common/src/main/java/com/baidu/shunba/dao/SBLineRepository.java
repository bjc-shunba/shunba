package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SBLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface SBLineRepository extends JpaRepository<SBLine, String>, JpaSpecificationExecutor<SBLine> {
    Optional<SBLine> findFirstByLineId(String lineId);

    List<SBLine> findAllByLineId(String lineId);

    List<SBLine> findAllByLineIdIn(Collection<String> lineId);
}
