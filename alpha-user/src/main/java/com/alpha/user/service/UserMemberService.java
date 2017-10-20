package com.alpha.user.service;

import com.alpha.server.rpc.user.pojo.UserMember;

import java.util.List;
import java.util.Map;

public interface UserMemberService {

    List<UserMember> find(Map<String, Object> param);

    void create(UserMember userMember);
}
