package com.alpha.user.dao.impl;

import com.alpha.commons.core.dao.impl.BaseDao;
import com.alpha.commons.core.sql.dto.DataRecord;
import com.alpha.commons.core.util.JavaBeanMap;
import com.alpha.commons.enums.DiagnosisStatus;
import com.alpha.commons.util.DateUtils;
import com.alpha.server.rpc.user.pojo.UserBasicRecord;
import com.alpha.user.dao.UserBasicRecordDao;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserBasicRecordDaoImpl extends BaseDao<UserBasicRecord, Long> implements UserBasicRecordDao {

    private static final String NAME_SPACE = "com.alpha.user.mapper.UserBasicRecordMapper";

    @SuppressWarnings("unchecked")
    @Override
    public UserBasicRecord findByDiagnosisId(Long diagnosisId) {
        String statement = NAME_SPACE + ".findByDiagnosisId";
        Map<String, Object> param = new HashMap<>();
        param.put("diagnosisId", diagnosisId);
        List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        UserBasicRecord record = null;
        if (!CollectionUtils.isEmpty(resultList)) {
            record = resultList.get(0);
        }
        return record;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UserBasicRecord> findByLastMonth(Map<String, Object> param) {
        String statement = NAME_SPACE + ".findByLastMonth";
        List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        return resultList;
    }

    public UserBasicRecordDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<UserBasicRecord> getClz() {
        return UserBasicRecord.class;
    }

    @SuppressWarnings("unchecked")
    @Override
    public UserBasicRecord findLast(Long userId) {
        String statement = NAME_SPACE + ".findLast";
        Map<String, Object> param = new HashMap<>();
        param.put("userId", userId);
        List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        UserBasicRecord record = new UserBasicRecord();
        if (!CollectionUtils.isEmpty(resultList)) {
            record = resultList.get(0);
        }
        return record;
    }
    
    @SuppressWarnings("unchecked")
	@Override
	public UserBasicRecord findLastCompleted(Long userId) {
    	 String statement = NAME_SPACE + ".findLast";
         Map<String, Object> param = new HashMap<>();
         param.put("userId", userId);
         //param.put("presentIllnessHistory", "1");
         param.put("status", DiagnosisStatus.PRE_DIAGNOSIS_FINISH.getValue());
         List<DataRecord> list = super.selectForList(statement, param);
         List<UserBasicRecord> resultList = new ArrayList<>();
         if (resultList != null) {
             resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
         }
         UserBasicRecord record = new UserBasicRecord();
         if (!CollectionUtils.isEmpty(resultList)) {
             record = resultList.get(0);
         }
         return record;
	}

    @Override
    public String findTemplateId(Long diagnosisId) {
        Map<String, Object> param = new HashMap<>();
        param.put("diagnosisId", diagnosisId);
        String statement = NAME_SPACE + ".findTemplateId";
        String templateId = super.selectForObject(statement, param);
        return templateId;
    }

	@SuppressWarnings("unchecked")
	@Override
	public UserBasicRecord findFinishByUserId(Long userId, String hisRegisterNo) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("status", DiagnosisStatus.PRE_DIAGNOSIS_FINISH.getValue());
		param.put("hisRegisterNo", hisRegisterNo);
		String statement = NAME_SPACE + ".findFinishByUser";
		List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        UserBasicRecord record = null;
        if (!CollectionUtils.isEmpty(resultList)) {
            record = resultList.get(0);
        }
        return record;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBasicRecord> listFinishByUserId(Long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("status", DiagnosisStatus.PRE_DIAGNOSIS_FINISH.getValue());
		param.put("hisRegisterNo", "hisRegisterNo");
		String statement = NAME_SPACE + ".findFinishByUser";
		List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        return resultList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<UserBasicRecord> listTodayFinishByUserId(Long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		param.put("status", DiagnosisStatus.PRE_DIAGNOSIS_FINISH.getValue());
		param.put("finishDate", DateUtils.date2String(new Date(), DateUtils.DATE_FORMAT));
		String statement = NAME_SPACE + ".findFinishByUser";
		List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        return resultList;
	}

	@Override
	public List<UserBasicRecord> listTodayUnConfirm() {
		Map<String, Object> param = new HashMap<>();
		param.put("status", DiagnosisStatus.HIS_CONFIRMED.getValue());
		String statement = NAME_SPACE + ".findUnComfirmOnToday";
		List<UserBasicRecord> resultList = executeBatch(statement, param);
        return resultList;
	}

	@Override
	public List<UserBasicRecord> listByUserId(Long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		String statement = NAME_SPACE + ".findByUserId";
		return executeBatch(statement, param);
	}
	
	@Override
	public UserBasicRecord getLastFinishByUserId(Long userId) {
		Map<String, Object> param = new HashMap<>();
		param.put("userId", userId);
		String statement = NAME_SPACE + ".findLastFinishByUser";
		Object obj = super.selectOne(statement, param);
		UserBasicRecord record = null;
		if(obj != null) {
			record = (UserBasicRecord) obj;
		}
		return record;
	}
	
	@SuppressWarnings("unchecked")
	private List<UserBasicRecord> executeBatch(String statement, Map<String, Object> param) {
		List<DataRecord> list = super.selectForList(statement, param);
        List<UserBasicRecord> resultList = new ArrayList<>();
        if (resultList != null) {
            resultList = JavaBeanMap.convertListToJavaBean(list, UserBasicRecord.class);
        }
        return resultList;
	}

}
