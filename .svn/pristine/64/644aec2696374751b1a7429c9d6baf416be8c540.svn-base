package com.alpha.user.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alpha.server.rpc.user.pojo.UserMember;
import com.alpha.user.dao.UserMemberDao;
import com.alpha.user.service.UserMemberService;

@Service
@Transactional
public class UserMemberServiceImpl implements UserMemberService {
	
	@Resource
	private UserMemberDao userMemberDao;
	
	@Override
	public List<UserMember> find(Map<String, Object> map) {
		List<UserMember> list = userMemberDao.find(map);
		return list;
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void create(UserMember userMember) {
		userMember.setCreateTime(new Date());
		userMemberDao.insert(userMember);
	}

}
