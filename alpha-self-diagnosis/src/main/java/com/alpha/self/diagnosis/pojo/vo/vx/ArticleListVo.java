package com.alpha.self.diagnosis.pojo.vo.vx;

import java.io.Serializable;
import java.util.Date;

import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisArticle;

/**
 * 微信公众号-信息确认-就诊信息-精彩资讯列表实体类
 * @author Administrator
 *
 */
public class ArticleListVo implements Serializable {

	private static final long serialVersionUID = -363499321565849408L;

	private Long id;
	
	private String articleCode;
	
	private String articleTitle;
	
	private String articleTitle2;
	
	private String articleContent;
	
	private String articleIcon;
	
	private Date createTime;
	
	private Date updateTime;
	
	public ArticleListVo(DiagnosisArticle article) {
		BeanCopierUtil.copy(article, this);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArticleCode() {
		return articleCode;
	}

	public void setArticleCode(String articleCode) {
		this.articleCode = articleCode;
	}

	public String getArticleTitle() {
		return articleTitle;
	}

	public void setArticleTitle(String articleTitle) {
		this.articleTitle = articleTitle;
	}

	public String getArticleTitle2() {
		return articleTitle2;
	}

	public void setArticleTitle2(String articleTitle2) {
		this.articleTitle2 = articleTitle2;
	}

	public String getArticleContent() {
		return articleContent;
	}

	public void setArticleContent(String articleContent) {
		this.articleContent = articleContent;
	}

	public String getArticleIcon() {
		return articleIcon;
	}

	public void setArticleIcon(String articleIcon) {
		this.articleIcon = articleIcon;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	
}
