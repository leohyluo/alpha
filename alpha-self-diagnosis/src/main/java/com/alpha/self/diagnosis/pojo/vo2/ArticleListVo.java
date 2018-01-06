package com.alpha.self.diagnosis.pojo.vo2;

import com.alpha.commons.util.BeanCopierUtil;
import com.alpha.server.rpc.diagnosis.pojo.DiagnosisArticle;

/**
 * 精选资讯列表
 * @author Administrator
 */
public class ArticleListVo {

	//文章编码
	private String articleCode;
	
	//文章标题
	private String articleTitle;
	
	//文章副标题
	private String articleTitle2;
	
	//文章icon
	private String articleIcon;
	
	public ArticleListVo() {}
	
	public ArticleListVo(DiagnosisArticle article) {
		BeanCopierUtil.copy(article, this);
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

	public String getArticleIcon() {
		return articleIcon;
	}

	public void setArticleIcon(String articleIcon) {
		this.articleIcon = articleIcon;
	}
	
	
}
