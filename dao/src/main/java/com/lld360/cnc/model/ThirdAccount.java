package com.lld360.cnc.model;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.Date;

/**
 * ThirdAccount
 */
public class ThirdAccount implements Serializable{
	/**
	 * id
	 */
	@NotBlank(message = "id 不能为空。")
	private Long id;	
	/**
	 * userId
	 */
	private Long userId;	
	/**
	 * openid
	 */
	@NotBlank(message = "openid 不能为空。")
	@Length(max = 50,message = "openid 最大长度不能超过50个字符")
	private String openid;	
	/**
	 * unionid
	 */
	@Length(max = 50,message = "unionid 最大长度不能超过50个字符")
	private String unionid;	
	/**
	 * type
	 */
	@NotBlank(message = "type 不能为空。")
	private Integer type;	
	/**
	 * nickname
	 */
	@NotBlank(message = "nickname 不能为空。")
	@Length(max = 45,message = "nickname 最大长度不能超过45个字符")
	private String nickname;	
	/**
	 * icon
	 */
	@Length(max = 100,message = "icon 最大长度不能超过100个字符")
	private String icon;	
	/**
	 * gender
	 */
	private Byte gender;	
	/**
	 * age
	 */
	private Byte age;	
	/**
	 * province
	 */
	@Length(max = 100,message = "province 最大长度不能超过100个字符")
	private String province;	
	/**
	 * city
	 */
	@Length(max = 100,message = "city 最大长度不能超过100个字符")
	private String city;	
	/**
	 * accessToken
	 */
	@NotBlank(message = "accessToken 不能为空。")
	@Length(max = 50,message = "accessToken 最大长度不能超过50个字符")
	private String accessToken;	
	/**
	 * refreshToken
	 */
	@NotBlank(message = "refreshToken 不能为空。")
	@Length(max = 50,message = "refreshToken 最大长度不能超过50个字符")
	private String refreshToken;	
	/**
	 * expiresIn
	 */
	@NotBlank(message = "expiresIn 不能为空。")
	@Length(max = 50,message = "expiresIn 最大长度不能超过50个字符")
	private Integer expiresIn;

	private Date createTime;
	private Date updateTime;

	private String code;
	private String state;
	private Integer errcode;
	private String errmsg;
	
	public void setUserId(Long value) {
		this.userId = value;
	}
	
	public Long getUserId() {
		return this.userId;
	}
	public void setOpenid(String value) {
		this.openid = value;
	}
	
	public String getOpenid() {
		return this.openid;
	}
	public void setUnionid(String value) {
		this.unionid = value;
	}
	
	public String getUnionid() {
		return this.unionid;
	}
	public void setType(Integer value) {
		this.type = value;
	}
	
	public Integer getType() {
		return this.type;
	}
	public void setNickname(String value) {
		this.nickname = value;
	}
	
	public String getNickname() {
		return this.nickname;
	}
	public void setIcon(String value) {
		this.icon = value;
	}
	
	public String getIcon() {
		return this.icon;
	}
	public void setGender(Byte value) {
		this.gender = value;
	}
	
	public Byte getGender() {
		return this.gender;
	}
	public void setAge(Byte value) {
		this.age = value;
	}
	
	public Byte getAge() {
		return this.age;
	}
	public void setProvince(String value) {
		this.province = value;
	}
	
	public String getProvince() {
		return this.province;
	}
	public void setCity(String value) {
		this.city = value;
	}
	
	public String getCity() {
		return this.city;
	}
	public void setAccessToken(String value) {
		this.accessToken = value;
	}
	
	public String getAccessToken() {
		return this.accessToken;
	}
	public void setRefreshToken(String value) {
		this.refreshToken = value;
	}
	
	public String getRefreshToken() {
		return this.refreshToken;
	}

	public Integer getExpiresIn() {
		return expiresIn;
	}

	public void setExpiresIn(Integer expiresIn) {
		this.expiresIn = expiresIn;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Integer getErrcode() {
		return errcode;
	}

	public void setErrcode(Integer errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
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