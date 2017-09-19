package com.alpha.user.service;

import java.util.List;
import java.util.Map;

import com.alpha.server.rpc.user.pojo.UserMember;

public interface UserMemberService {

	List<UserMember> find(Map<String, Object> param);
	
	void create(UserMember userMember);
}
