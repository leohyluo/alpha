package com.alpha.user.service.impl;

import java.util.Date;
import java.util.List;

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
    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public void create(UserMember userMember) {
        userMember.setCreateTime(new Date());
        userMemberDao.insert(userMember);
    }

	@Override
	public List<UserMember> listByUserId(Long userId) {
		return userMemberDao.listByUserId(userId);
	}

	@Override
	public List<UserMember> listByUserIdAndMemberName(Long userId, String memberName) {
		return userMemberDao.listByUserIdAndMemberName(userId, memberName);
	}

}
