package com.alpha.self.diagnosis.processor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alpha.commons.enums.BasicQuestionType;
import com.alpha.self.diagnosis.pojo.BasicQuestion;
import com.alpha.self.diagnosis.pojo.vo.BasicQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.IQuestionVo;
import com.alpha.self.diagnosis.pojo.vo.LiverRenalQuestionVo;
import com.alpha.server.rpc.user.pojo.UserInfo;

public class IQuestionVoBuilder {
	
	private static Map<BasicQuestionType, Class<?>> map;
	private static Map<BasicQuestionType, Class<?>[]> parameterTypesMap;
	
	static {
		map = new HashMap<>();
		parameterTypesMap = new HashMap<>();
		
		map.put(BasicQuestionType.DEFAULT, BasicQuestionVo.class);
		parameterTypesMap.put(BasicQuestionType.DEFAULT, new Class<?>[]{Long.class, BasicQuestion.class, List.class, UserInfo.class});
		
		map.put(BasicQuestionType.LIVER_RENAL, LiverRenalQuestionVo.class);
		parameterTypesMap.put(BasicQuestionType.LIVER_RENAL, new Class<?>[]{Long.class, BasicQuestion.class, List.class, List.class});
	}

	public static IQuestionVo build(BasicQuestion basicQuestion, Object... initargs) {
		Class<?> clazz = null;
		Class<?>[] parameterTypes = null;
		String questionCode = basicQuestion.getQuestionCode();
		BasicQuestionType questionType = BasicQuestionType.findByValue(questionCode);
		if(questionType == null) {
			questionType = BasicQuestionType.DEFAULT;
		}
		clazz = map.get(questionType);
		parameterTypes = parameterTypesMap.get(questionType);
		
		IQuestionVo result = null;
		try {
			Constructor<?> constructor = clazz.getConstructor(parameterTypes);
			Object object = constructor.newInstance(initargs);
			result = (IQuestionVo) object;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
