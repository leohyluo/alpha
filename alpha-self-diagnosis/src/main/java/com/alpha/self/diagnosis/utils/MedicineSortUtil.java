package com.alpha.self.diagnosis.utils;

import com.alpha.self.diagnosis.pojo.vo.BasicAnswerVo;
import com.alpha.self.diagnosis.pojo.vo.IAnswerVo;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisQuestionAnswer;
import com.alpha.server.rpc.diagnosis.pojo.UserDiagnosisOutcome;
import com.alpha.server.rpc.diagnosis.pojo.vo.MedicineQuestionVo;

import java.util.*;

/**
 * Created by xc.xiong on 2017/9/8.
 */
public class MedicineSortUtil {


    /**
     * 计算答案权重，并排序
     *
     * @param dqAnswers
     */
    public static LinkedHashSet<IAnswerVo> sortAnswer(List<DiagnosisQuestionAnswer> dqAnswers) {

        //计算问题权重
        //          1、查询类型为101的 的所有问题
        //2、取出所有的特异性疾病，分为三组，正、空、反
        //3、每组进行权重计算，问题权重+答案权重，正向排序；
        //4、过滤重复，返回答案

        //分特异性 正、空、反
        TreeMap<Integer, Set<DiagnosisQuestionAnswer>> answerSpecMap = new TreeMap<>();
        Set<String> questionCodes = new HashSet<>();
        for (DiagnosisQuestionAnswer dqa : dqAnswers) {
            Set<DiagnosisQuestionAnswer> specSet = answerSpecMap.get(dqa.getAnswerSpec()) == null ? new HashSet<>() : answerSpecMap.get(dqa.getAnswerSpec());
            specSet.add(dqa);
            answerSpecMap.put(dqa.getAnswerSpec(), specSet);
            questionCodes.add(dqa.getQuestionCode());
        }
        //排序
        answerSpecMap = MedicineSortUtil.sortAnswerSpecMap(answerSpecMap);

        //计算权重
        for (Map.Entry<Integer, Set<DiagnosisQuestionAnswer>> entry : answerSpecMap.entrySet()) {
            Set<DiagnosisQuestionAnswer> dqAnswerSet = entry.getValue();
            for (DiagnosisQuestionAnswer dqAnswer : dqAnswerSet) {
                dqAnswer.setWeightValue(dqAnswer.getQuestionWeight() * dqAnswer.getWeight());
            }
            //答案排序
            dqAnswerSet = MedicineSortUtil.sortAnswer(dqAnswerSet);
            answerSpecMap.put(entry.getKey(), dqAnswerSet);
        }
        //去重复
        LinkedHashSet<IAnswerVo> answers = new LinkedHashSet<>();
        for (Map.Entry<Integer, Set<DiagnosisQuestionAnswer>> entry : answerSpecMap.entrySet()) {
            Set<DiagnosisQuestionAnswer> dqAnswerSet = entry.getValue();
            List<IAnswerVo> answerVos = BasicAnswerVo.convertListMedicineAnswerVo(dqAnswerSet);
            answers.addAll(answerVos);
        }
        return answers;
    }

    /**
     * 答案权重排序
     *
     * @param dqAnswerSet
     */
    public static Set<DiagnosisQuestionAnswer> sortAnswer(Set<DiagnosisQuestionAnswer> dqAnswerSet) {
        List<DiagnosisQuestionAnswer> dqAnswers = new ArrayList<>(dqAnswerSet);
        Collections.sort(dqAnswers, new Comparator<DiagnosisQuestionAnswer>() {
            @Override
            public int compare(DiagnosisQuestionAnswer o1, DiagnosisQuestionAnswer o2) {
                return (int) (o2.getWeightValue() - o1.getWeightValue());
            }
        });
        return new LinkedHashSet<>(dqAnswers);
    }

    /**
     * 答案 特异性培训
     *
     * @param answerSpecMap
     * @return
     */
    public static TreeMap sortAnswerSpecMap(TreeMap<Integer, Set<DiagnosisQuestionAnswer>> answerSpecMap) {
        //排序
        TreeMap<Integer, Set<DiagnosisQuestionAnswer>> sortMap = new TreeMap<>(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                return o2 - o1;
            }
        });
        sortMap.putAll(answerSpecMap);
        return sortMap;
    }

    /**
     * 答案排序
     *
     * @param answerVos
     * @return
     */
    public static List<IAnswerVo> sortAnswerVo(LinkedHashSet<IAnswerVo> answerVos) {
        List<IAnswerVo> answers = new ArrayList<>(answerVos);
        Collections.sort(answers, new Comparator<IAnswerVo>() {
            @Override
            public int compare(IAnswerVo o1, IAnswerVo o2) {
                BasicAnswerVo answerVo1 = (BasicAnswerVo) o1;
                BasicAnswerVo answerVo2 = (BasicAnswerVo) o2;
                return (answerVo2.getDefaultOrder() - answerVo1.getDefaultOrder());
            }
        });
        return answers;
    }

    /**
     * @param userDiagnosisOutcomes
     */
    public static void sortUserDiagnosisOutcome(List<UserDiagnosisOutcome> userDiagnosisOutcomes) {
        Collections.sort(userDiagnosisOutcomes, new Comparator<UserDiagnosisOutcome>() {
            @Override
            public int compare(UserDiagnosisOutcome o1, UserDiagnosisOutcome o2) {
                if (o1.getAnswerSpec() == o2.getAnswerSpec()) {
                    return (int) (o2.getWeight() - o1.getWeight());
                } else {
                    return (o2.getAnswerSpec() - o1.getAnswerSpec());
                }

            }
        });
    }

    public static void sortMedicineQuestionVoByAnswerWeight(List<MedicineQuestionVo> questionVos) {
        Collections.sort(questionVos, new Comparator<MedicineQuestionVo>() {
            @Override
            public int compare(MedicineQuestionVo o1, MedicineQuestionVo o2) {
                return (int) (o2.getAnswerWeight() - o2.getAnswerWeight());
            }
        });
    }

    public static void sortMedicineQuestionVoByQuestionWeight(List<MedicineQuestionVo> questionVos) {
        Collections.sort(questionVos, new Comparator<MedicineQuestionVo>() {
            @Override
            public int compare(MedicineQuestionVo o1, MedicineQuestionVo o2) {
                return (int) (o2.getQuestionWeight() - o2.getQuestionWeight());
            }
        });
    }
}
