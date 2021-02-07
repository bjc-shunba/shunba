package com.baidu.shunba.service.impl;

import com.baidu.shunba.dao.SBMemberRepository;
import com.baidu.shunba.entity.SBMember;
import com.baidu.shunba.service.SBMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SBMemberServiceImpl extends BaseServiceImpl<SBMember, String> implements SBMemberService {
    @Autowired
    private SBMemberRepository sbMemberRepository;

    @Override
    protected JpaRepository<SBMember, String> getJpaRepository() {
        return sbMemberRepository;
    }

    @Override
    protected JpaSpecificationExecutor<SBMember> getJpaSpecificationExecutor() {
        return sbMemberRepository;
    }

    @Override
    public Optional<SBMember> findByMemberId(String memberId) {
        return sbMemberRepository.findByMemberId(memberId);
    }
}
