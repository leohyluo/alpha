package com.alpha.user.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.server.rpc.user.pojo.UserFeedback;
import com.alpha.user.dao.UserFeedbackDao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class UserFeedbackDaoImpl extends BaseDao<UserFeedback, Long> implements UserFeedbackDao {

    private static final String NAME_SPACE = "com.alpha.server.rpc.user.pojo.UserFeedback";


    public UserFeedbackDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<UserFeedback> getClz() {
        return UserFeedback.class;
    }

}
