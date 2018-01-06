package com.alpha.user.dao;

import java.util.List;

import com.alpha.commons.core.dao.IBaseDao;
import com.alpha.commons.enums.System;
import com.alpha.user.pojo.UserFeedBackItem;

public interface UserFeedBackItemDao extends IBaseDao<UserFeedBackItem, Long> {

	List<UserFeedBackItem> listUserFeedBackItem(System system);
}
