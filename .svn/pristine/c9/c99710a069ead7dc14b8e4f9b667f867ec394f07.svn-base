package com.alpha.self.diagnosis.dao.impl;

        import com.alpha.commons.core.dao.impl.BaseDao;
        import com.alpha.commons.core.sql.dto.DataRecord;
        import com.alpha.commons.core.util.JavaBeanMap;
        import com.alpha.self.diagnosis.dao.DiagnosisDiseaseDao;
        import com.alpha.server.rpc.diagnosis.pojo.DiagnosisDisease;
        import org.apache.ibatis.session.SqlSessionFactory;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.stereotype.Repository;

        import java.util.Collection;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;

/**
 * Created by xc.xiong on 2017/9/5.
 */
@Repository
public class DiagnosisDiseaseDaoImpl extends BaseDao<DiagnosisDisease, Long> implements DiagnosisDiseaseDao {


    @Autowired
    public DiagnosisDiseaseDaoImpl(SqlSessionFactory sqlSessionFactory) {
        super(sqlSessionFactory);
    }

    @Override
    public Class<DiagnosisDisease> getClz() {
        return DiagnosisDisease.class;
    }

    /**
     * 查询所有的疾病
     *
     * @param diseaseCodes
     * @return
     */
    public List<DiagnosisDisease> listDiagnosisDisease(Collection diseaseCodes) {
        if (diseaseCodes.size() == 0)
            return null;
        Map<String, Object> params = new HashMap<>();
        params.put("diseaseCodes", diseaseCodes);
        List<DataRecord> datas = super.selectForList("com.alpha.server.rpc.diagnosis.pojo.DiagnosisDisease.queryDiagnosisDisease", params);
        List<DiagnosisDisease> dds = JavaBeanMap.convertListToJavaBean(datas, DiagnosisDisease.class);
        return dds;
    }


}
