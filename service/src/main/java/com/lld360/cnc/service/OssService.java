package com.lld360.cnc.service;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.mts.model.v20140618.SearchMediaWorkflowResponse;
import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.bean.Credential;
import com.lld360.cnc.core.utils.AliYunOSSUtil;
import com.lld360.cnc.dto.SoftDownloads;
import com.lld360.cnc.model.SoftDoc;
import com.lld360.cnc.model.SoftUpload;
import com.lld360.cnc.repository.SoftFileDao;
import com.lld360.cnc.util.CredentialUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.*;

/**
 * Author: dhc
 * Date: 2016-12-20 16:05
 */
@Service
public class OssService extends BaseService {
    @Autowired
    private AliYunOSSUtil ossUtill;

    @Autowired
    private SoftFileDao softFileDao;

    // 根据前缀获取文件列表
    public List<OSSObjectSummary> getVideosByDir(String dir) {
        ObjectListing listing = ossUtill.getObjectListing(dir);
        return listing.getObjectSummaries();
    }

    // 获取媒体工作流
    public SearchMediaWorkflowResponse getMediaWorkflows() {
        return ossUtill.getMediaWorkflows();
    }

    //获得临时鉴权信息
    public Credential getCredential() throws ClientException {
        return CredentialUtil.getInstance().getCredential();
    }

    //创建文件夹
    public void makeUpDir(String fileDir) {
        // 创建OSSClient实例
        OSSClient client = CredentialUtil.getInstance().getOssClient();
        boolean found = client.doesObjectExist(CredentialUtil.BUCKET, fileDir);
        if (!found) {
            client.putObject(CredentialUtil.BUCKET, fileDir, new ByteArrayInputStream(new byte[0]));
        }
    }

    //删除对象
    public void deleteObject(String object) {
        deleteObject(CredentialUtil.BUCKET, object);
    }

    public void deleteObject(String bucket, String object) {
        OSSClient client = CredentialUtil.getInstance().getOssClient();
        client.deleteObject(bucket, object);
    }

    //判断对象是否存在
    public boolean isExist(String object) {
        return isExist(CredentialUtil.BUCKET, object);
    }

    public boolean isExist(String bucket, String object) {
        OSSClient client = CredentialUtil.getInstance().getOssClient();
        return client.doesObjectExist(bucket, object);
    }

    public URL getObjectUrl(String object, Date expiration) {
        OSSClient client = CredentialUtil.getInstance().getOssClient();
        if (client.doesObjectExist(CredentialUtil.BUCKET, object)) {
            return client.generatePresignedUrl(CredentialUtil.BUCKET, object, expiration);
        } else {
            throw new ServerException(HttpStatus.NOT_FOUND, M.E10105);
        }
    }

    public URL getObjectUrl(String object) {
        Calendar calendar = Calendar.getInstance(Locale.CHINA);
        calendar.add(Calendar.MINUTE, 15);
        return getObjectUrl(object, calendar.getTime());
    }

    public boolean isExist(List<SoftDoc> softs) {
        for (SoftDoc soft : softs) {
            if (!isExist(soft.getFilePath())) {
                return false;
            }
        }
        return true;
    }

    public List<SoftDownloads> getObjectUrl(SoftUpload doc) {
        List<SoftDoc> softs = softFileDao.findByUuId(doc.getUuId());
        List<SoftDownloads> downloads = new ArrayList<>();
        for (SoftDoc soft : softs) {
            URL url = getObjectUrl(soft.getFilePath());
            SoftDownloads download = new SoftDownloads();
            download.setSoft(soft);
            download.setUrl(url);
            downloads.add(download);
        }
        return downloads;
    }

    public void deleteObject(SoftUpload doc) {
        List<SoftDoc> softs = softFileDao.findByUuId(doc.getUuId());
        for (SoftDoc soft : softs) {
            deleteObject(soft.getFilePath());
        }
    }
}
