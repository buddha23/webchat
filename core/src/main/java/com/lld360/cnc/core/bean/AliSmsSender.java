package com.lld360.cnc.core.bean;

import com.lld360.cnc.core.Configer;
import com.lld360.cnc.core.utils.JsonUtils;
import com.taobao.api.ApiException;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.AlibabaAliqinFcSmsNumSendRequest;
import com.taobao.api.response.AlibabaAliqinFcSmsNumSendResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Author: dhc
 * Date: 2016-02-25 17:03
 */
public class AliSmsSender {
    public static final String SMS_TPL_VALIDCODE = "SMS_47390252";

    @Autowired
    private Configer configer;

    // 发送短信
    public String sendSms(String templateId, Map<String, String> content, String... mobiles) throws ApiException {
        if (null == mobiles || mobiles.length == 0) return "error: mobiles is null";

        String contentString = "";
        if (content != null && !content.isEmpty()) {
            contentString = JsonUtils.toJson(content);
        }

        List<String> list = Arrays.stream(mobiles).distinct().collect(Collectors.toList());

        int num = 200;//最多一次200个号码
        int page = (list.size() + num - 1) / num;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < page; i++) {
            String mobilesStr = list.stream().limit((i + 1) * num).skip(i * num).reduce((str1, str2) -> str1 + "," + str2).get();
            result.append("batch-").append(i).append(": ").append(sendSms(templateId, contentString, mobilesStr)).append("; ");
        }
        return result.toString();
    }

    // 发送短信
    public String sendSms(String templateId, String context, String mobiles) throws ApiException {
        TaobaoClient client = configer.getTaobaoSmsClient();
        AlibabaAliqinFcSmsNumSendRequest req = new AlibabaAliqinFcSmsNumSendRequest();
        req.setExtend("");
        req.setSmsType("normal");
        req.setSmsFreeSignName("大牛数控");
        if (!StringUtils.isEmpty(context)) req.setSmsParamString(context);
        req.setRecNum(mobiles);
        req.setSmsTemplateCode(templateId);
        AlibabaAliqinFcSmsNumSendResponse rsp = client.execute(req);
        return rsp.getBody();
    }
}
