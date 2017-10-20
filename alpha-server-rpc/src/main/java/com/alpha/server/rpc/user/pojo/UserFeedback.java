package com.alpha.server.rpc.user.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户反馈内容
 */
@Entity
@Table(name = "user_feedback")
public class UserFeedback implements Serializable {

    private static final long serialVersionUID = 4088943806498571219L;

    /**
     * id
     */
    @Column(name = "id")
    @Id
    private Long id;

    /**
     * 反馈人的ID
     */
    @Column(name = "user_id")
    private Long userId;

    /**
     * 反馈内容
     */
    @Column(name = "content")
    private String content;

    /**
     * 反馈时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 诊断编号
     */
    @Column(name = "diagnosis_id")
    private Long diagnosisId;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(Long diagnosisId) {
        this.diagnosisId = diagnosisId;
    }
}
