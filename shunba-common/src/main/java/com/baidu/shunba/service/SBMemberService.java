package com.baidu.shunba.service;

import com.baidu.shunba.entity.SBMember;

import java.util.Optional;

public interface SBMemberService extends BaseService<SBMember, String> {
    Optional<SBMember> findByMemberId(String memberId);
}
