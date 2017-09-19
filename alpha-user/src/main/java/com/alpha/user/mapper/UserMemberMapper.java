package com.alpha.user.mapper;

import java.util.List;

import com.alpha.server.rpc.user.pojo.UserMember;

public interface UserMemberMapper {

	List<UserMember> find(UserMember userMember);
}
