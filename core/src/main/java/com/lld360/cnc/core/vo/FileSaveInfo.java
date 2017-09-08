package com.lld360.cnc.core.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 保存文件并返回文件信息
 */
public class FileSaveInfo {
    private long size;
    private String originName;
    private String baseName;
    private String fileName;
    private String relativePath;
    @JsonIgnore
    private String fullPath;
    private String extension;
    @JsonIgnore
    private File file;

    /**
     * 初始化一个上传文件信息并保存文件
     *
     * @param file         上传的文件
     * @param basePath     基本文件路径
     * @param relativePath 保存的相对位置
     * @param targetName   保存目标名称，如果为空，则为原文件名
     * @throws IOException 保存文件失败
     */
    public FileSaveInfo(MultipartFile file, String basePath, String relativePath, String targetName) throws IOException {
        if (file == null) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "输入文件不能为空");
        }
        this.size = file.getSize();
        this.originName = file.getOriginalFilename();
        if (StringUtils.isNotEmpty(relativePath)) {
            relativePath = relativePath.replaceAll("^/+|/+$", "");
        }
        this.extension = FilenameUtils.getExtension(this.originName);
        this.baseName = StringUtils.isEmpty(targetName) ? this.originName : targetName;
        this.fileName = this.baseName;
        if (StringUtils.isNotEmpty(this.extension)) {
            this.fileName += '.' + this.extension;
        }
        this.relativePath = this.fileName;
        if (StringUtils.isNotEmpty(relativePath)) {
            this.relativePath = relativePath + '/' + this.fileName;
        }
        if (basePath.endsWith("/")) {
            basePath += '/';
        }
        this.fullPath = basePath + this.relativePath;
        this.file = new File(this.fullPath);
        File dir = this.file.getParentFile();
        if (dir.isDirectory() || dir.mkdirs()) {
            file.transferTo(this.file);
        } else {
            throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10102).setData(this.fullPath);
        }
    }

    public long getSize() {
        return size;
    }

    public String getOriginName() {
        return originName;
    }

    public String getBaseName() {
        return baseName;
    }

    public String getFileName() {
        return fileName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getExtension() {
        return extension;
    }

    public File getFile() {
        return file;
    }

    public void delete() {
        if (file != null) {
            if (file.isFile() && file.delete()) {
                file = null;
            }
        }
    }
}
