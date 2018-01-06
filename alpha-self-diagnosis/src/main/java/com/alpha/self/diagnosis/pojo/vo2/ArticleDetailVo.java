package com.alpha.self.diagnosis.pojo.vo2;

import java.util.Date;

/**
 * 资讯详情
 * @author Administrator
 *
 */
public class ArticleDetailVo {

	private String articleCode;
	
	private String articleTitle;
	
	private String articleTitle2;
	
	private String articleContent;
	
	private String articleIcon;
	
	//作者
	private String author;
	
	//来源
	private String source;
	
	private Date createTime;
	
	private Date updateTime;

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

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
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
