package com.alpha.server.rpc.diagnosis.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "hospital_info")
public class HospitalInfo {

	/**
     * id
     */
    @Column(name = "id")
    @Id
    private Long id;
    
    @Column(name = "hospital_code")
    private String hospitalCode;
    
    @Column(name = "hospital_name")
    private String hospitalName;
    
    @Column(name = "hospital_icon")
    private String hospitalIcon;
    
    @Column(name = "create_time")
    private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHospitalCode() {
		return hospitalCode;
	}

	public void setHospitalCode(String hospitalCode) {
		this.hospitalCode = hospitalCode;
	}

	public String getHospitalName() {
		return hospitalName;
	}

	public void setHospitalName(String hospitalName) {
		this.hospitalName = hospitalName;
	}

	public String getHospitalIcon() {
		return hospitalIcon;
	}

	public void setHospitalIcon(String hospitalIcon) {
		this.hospitalIcon = hospitalIcon;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	} 
    
    
}
