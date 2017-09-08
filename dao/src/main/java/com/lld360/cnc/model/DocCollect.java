package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * DocCollect
 */
public class DocCollect implements Serializable{
	/**
	 * 用户ID
	 */
	@NotBlank(message = "用户ID 不能为空。")
	private Long userId;	
	/**
	 * 文档ID
	 */
	@NotBlank(message = "文档ID 不能为空。")
	private Long docId;	
	/**
	 * 收藏标题
	 */
	@Length(max = 100,message = "收藏标题 最大长度不能超过100个字符")
	private String title;	
	/**
	 * 收藏备注
	 */
	@Length(max = 200,message = "收藏备注 最大长度不能超过200个字符")
	private String remark;	
	/**
	 * 收藏时间
	 */
	@NotBlank(message = "收藏时间 不能为空。")
	private Date createTime;	
	
	
	public void setTitle(String value) {
		this.title = value;
	}
	
	public String getTitle() {
		return this.title;
	}
	public void setRemark(String value) {
		this.remark = value;
	}
	
	public String getRemark() {
		return this.remark;
	}
	public void setCreateTime(Date value) {
		this.createTime = value;
	}
	
	public Date getCreateTime() {
		return this.createTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getDocId() {
		return docId;
	}

	public void setDocId(Long docId) {
		this.docId = docId;
	}
}