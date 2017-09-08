package com.lld360.cnc.model;
import java.io.Serializable;
import java.util.Date;
import org.hibernate.validator.constraints.Length;

public class SoftDoc implements Serializable{
	
	private Long id;
	
	private Long uuId;
	   /**
     * 文件名称
     */
    @Length(max = 120, message = "文件名称 最大长度不能超过120个字符")
    private String fileName;
    /**
     * 文件地址
     */
    @Length(max = 150, message = "文件地址 最大长度不能超过150个字符")
    private String filePath;
    /**
     * 文件类型
     */
    @Length(max = 10, message = "文件类型 最大长度不能超过10个字符")
    private String fileType;
    /**
     * 文件大小（KB)
     */
    private Long fileSize;
    /**
     * 文件MD5
     */
    @Length(max = 32, message = "文件MD5 最大长度不能超过32个字符")
    private String fileMd5;
    /**
     * 文档页数
     */
    
    private Date createTime;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getUuId() {
		return uuId;
	}
	public void setUuId(Long uuId) {
		this.uuId = uuId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public Long getFileSize() {
		return fileSize;
	}
	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}
	public String getFileMd5() {
		return fileMd5;
	}
	public void setFileMd5(String fileMd5) {
		this.fileMd5 = fileMd5;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
