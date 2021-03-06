package com.alpha.self.diagnosis.pojo.enums;

/**
 * Created by xc.xiong on 2017/9/1.
 * 问题类型
 */
public enum QuestionEnum {

    介绍信息(10),//Hi，我是基于国家十二五863课题成果转化，由庞大的医学数据支持的阿尔法医生问诊机器人，愿意为您效劳。  单选，两个入口，返回多个文本信息，
    出生日期(22),
    性别(23),//头像资源放本地
    肝肾功能(24),
    姓名(26),
    体重(25),
    主症状语义分析(99),
    主症状(100),
    医学问题(101),
    伴随症状(102),
    年龄问题(103),
    季节问题(104),
    常见伴随症状(105),
    诊断结果(200);

    private Integer value;

    QuestionEnum(Integer value) {
        this.value = value;
    }

    public int getOrdinal() {
        return this.ordinal();
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public static QuestionEnum getQuestion(int type) {
        QuestionEnum qs[] = QuestionEnum.values();
        for (QuestionEnum q : qs) {
            if (q.getValue() == type)
                return q;
        }
        return null;
    }
}
