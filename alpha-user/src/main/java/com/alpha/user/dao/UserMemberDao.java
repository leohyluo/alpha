package com.alpha.user.dao;

import java.util.List;
import java.util.Map;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.server.rpc.user.pojo.UserMember;

public interface UserMemberDao extends IBaseDao<UserMember, Long> {

	List<UserMember> find(Map<String, Object> map);
		
}
