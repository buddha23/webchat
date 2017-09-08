package com.lld360.cnc.service;

import com.lld360.cnc.BaseService;
import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.M;
import com.lld360.cnc.core.ServerException;
import com.lld360.cnc.core.bean.AliSmsSender;
import com.lld360.cnc.model.ValidSms;
import com.lld360.cnc.repository.ValidSmsDao;
import com.lld360.cnc.util.SMSChecker;
import com.taobao.api.ApiException;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
public class ValidSmsService extends BaseService {
    private Logger logger = LoggerFactory.getLogger(ValidSmsService.class);

    public static final int SMS_MAX_COUNT_PER_DAY = 10;
    public static final long MOBILE_KEEP_ALIVE_TIME = 86400000;     //1000 * 60 * 60 * 24;
    public static final int SMS_MAP_MAX_COUNT_PER_DAY = 1000;

    private static final Map<String, SMSChecker> MobileMap = new HashMap<>();


    @Autowired
    private ValidSmsDao validSmsDao;

    @Autowired
    private AliSmsSender aliSmsSender;

    @Autowired
    private Configer configer;

    public ValidSms get(String mobile, Byte type) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", type);
        return validSmsDao.find(params);
    }

    // 创建或更新短信验证码数据
    public ValidSms couSms(String mobile, Byte type) {

        /*每个账号每天验证码不超过一定次数
        * 每天不能够超过1000个手机发短信
        * */
        SMSChecker checker = MobileMap.get(mobile);
        if (checker == null) {
            if (MobileMap.size() > SMS_MAP_MAX_COUNT_PER_DAY) {
                logger.info("SMS_MAP_MAX,发送总数量今日过大：" + MobileMap.size());
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10008).setData("发送总数量今日过大,目前手机："+mobile);
            }
            checker = new SMSChecker();
            checker.setSendCount(1);
            checker.setFirstSendTime(new Date());
            MobileMap.put(mobile, checker);
        } else {
            if (checker.getSendCount() >= SMS_MAX_COUNT_PER_DAY) {
                logger.info("SMS_MAX,此手机发送数量过大：" + checker.getSendCount()+"，手机号："+mobile);
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10008).setData("此手机发送数量过大："+mobile);
            }
            checker.setSendCount(checker.getSendCount() + 1);
        }

        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", type);
        ValidSms sms = validSmsDao.find(params);
        Date now = new Date();
        long expiredTime;
        expiredTime = configer.getSmsExpiredTime();
        if (sms != null) {
            if (sms.getExpiredTime().getTime() < now.getTime()) {
                sms.setValidCode(RandomStringUtils.randomNumeric(6));
            }
            sms.setExpiredTime(new Date(now.getTime() + expiredTime * 60000));
            validSmsDao.update(sms);
        } else {
            sms = new ValidSms();
            sms.setMobile(mobile);
            sms.setType(type);
            sms.setExpiredTime(new Date(now.getTime() + expiredTime * 60000));
            sms.setValidCode(RandomStringUtils.randomNumeric(6));
            validSmsDao.create(sms);
        }

        logger.info(MessageFormat.format("发送短信 --> 手机号:{0}, 验证码:{1}, 类型:{2}", mobile, sms.getValidCode(), sms.getType()));
        if (configer.getEnv().equals("prod")) {
            try {
                Map<String, String> content = new HashMap<>();
                content.put("code", sms.getValidCode());
                content.put("expired", String.valueOf(configer.getSmsExpiredTime()));


                logger.info("发送短信结果：" + aliSmsSender.sendSms(AliSmsSender.SMS_TPL_VALIDCODE, content, mobile));
            } catch (ApiException e) {
                logger.warn(getMessage("E10008"), e);
                throw new ServerException(HttpStatus.INTERNAL_SERVER_ERROR, M.E10008).setData(e.getMessage());
            }
        }

        return sms;
    }

    // 验证短信
    public void validSmsCode(String mobile, byte type, String code) {
        validSmsCode(mobile, type, code, true);
    }

    public void validSmsCode(String mobile, byte type, String code, boolean autoDelete) {
        Map<String, Object> params = new HashMap<>();
        params.put("mobile", mobile);
        params.put("type", type);
        ValidSms sms = validSmsDao.find(params);
        Date now = new Date();
        if (sms == null || sms.getExpiredTime().getTime() < now.getTime()) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10009);
        }
        if (!sms.getValidCode().equals(code)) {
            throw new ServerException(HttpStatus.BAD_REQUEST, M.E10010);
        }
        // 验证成功删除短信验证码
        if (autoDelete)
            validSmsDao.delete(params);
    }


    /**
     *
     */
    public void clearMobileChecker() {
        Date now = new Date();
        Iterator it = MobileMap.values().iterator();
        while (it.hasNext()) {
            SMSChecker checker = (SMSChecker) it.next();
            Date firstDate = checker.getFirstSendTime();
            long diff = now.getTime() - firstDate.getTime();
            long days = diff / MOBILE_KEEP_ALIVE_TIME;
            if (days >= 1) {
                it.remove();
            }
        }
    }
}
