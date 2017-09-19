package com.alpha.commons.core.mapper;

import java.util.Map;

import com.alpha.commons.core.pojo.SysSequence;
import org.apache.ibatis.annotations.Param;

public interface SysSequenceMapper {

	SysSequence getSequence(@Param("sequenceKey") String sequenceKey);

	int updateCurrentValue(Map<String, Object> map);


}
