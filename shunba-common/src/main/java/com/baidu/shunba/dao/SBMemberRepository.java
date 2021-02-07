package com.baidu.shunba.dao;

import com.baidu.shunba.entity.SBMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SBMemberRepository extends JpaRepository<SBMember, String>, JpaSpecificationExecutor<SBMember> {
    Optional<SBMember> findByMemberId(String memberId);
}
