package com.lld360.cnc.service;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.xml.sax.InputSource;

import com.lld360.cnc.core.WeiXinPayConfiger;
import com.lld360.cnc.util.MD5Util;

@Service
public class WeixinPayService {

    public static final String CHARACTER_ENCODING = "UTF-8";

    private static final Logger logger = LoggerFactory.getLogger(WeixinPayService.class);
    @Autowired
    private WeiXinPayConfiger weiXinPayConfiger;

    public String unifiedorder(BigDecimal mount, String tradeNo, String ip, String openId) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", weiXinPayConfiger.getAppId());
        parameters.put("mch_id", weiXinPayConfiger.getMchId());
        parameters.put("device_info", "WEB");
        parameters.put("body", "cnc_weixinPay");
        parameters.put("nonce_str", RandomStringUtils.randomAlphanumeric(18));
        parameters.put("out_trade_no", tradeNo);

        //单位为分
        parameters.put("total_fee", mount.multiply(new BigDecimal("100")).intValue());
        parameters.put("spbill_create_ip", ip);
        parameters.put("notify_url", weiXinPayConfiger.getCallBackUrl());
        parameters.put("trade_type", "JSAPI");
        parameters.put("openid", openId);
        String sign = createSign(parameters);
        parameters.put("sign", sign);
        RestTemplate template = new RestTemplate();
        String body = parseToXmlString(parameters);
        String resultStr = template.postForObject(WeiXinPayConfiger.UNIFIED_ORDER, body, String.class);
        String prepayId = null;

        try {
            String returnMsg = getNodeInfo(resultStr, "return_msg");
            logger.info(new String(returnMsg.getBytes("ISO-8859-1"), "UTF-8"));
            prepayId = getNodeInfo(resultStr, "prepay_id");
        } catch (JDOMException | IOException e) {
            logger.error("unifiedorder.getPrepayId error", e);
        }
        return prepayId;
    }

    public String unifiedorderByQr(BigDecimal mount, String ip, String tradeNo) {
        logger.info("unifiedorderByQr in ....");
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", weiXinPayConfiger.getAppId());
        parameters.put("mch_id", weiXinPayConfiger.getMchId());
        parameters.put("device_info", "WEB");
        parameters.put("body", "cnc_weixinPay");
        parameters.put("nonce_str", RandomStringUtils.randomAlphanumeric(18));
        parameters.put("out_trade_no", tradeNo);
        //单位为分
        parameters.put("total_fee", mount.multiply(new BigDecimal("100")).intValue());
        parameters.put("spbill_create_ip", ip);
        parameters.put("notify_url", weiXinPayConfiger.getCallBackUrl());
        parameters.put("trade_type", "NATIVE");
        String sign = createSign(parameters);
        parameters.put("sign", sign);
        RestTemplate template = new RestTemplate();
        String body = parseToXmlString(parameters);
        String resultStr = template.postForObject(WeiXinPayConfiger.UNIFIED_ORDER, body, String.class);
        // String prepayId = null;
        String codeUrl = null;
        try {
            String returnMsg = getNodeInfo(resultStr, "return_msg");
            logger.info(new String(returnMsg.getBytes("ISO-8859-1"), "UTF-8"));
            codeUrl = getNodeInfo(resultStr, "code_url");
        } catch (JDOMException | IOException e) {
            logger.error("unifiedorder.getPrepayId error", e);
        }
        return codeUrl;
    }


    public String orderQuery(String innerTradeNo) {
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", weiXinPayConfiger.getAppId());
        parameters.put("mch_id", weiXinPayConfiger.getMchId());
        parameters.put("out_trade_no", innerTradeNo);
        //单位为分
        parameters.put("nonce_str", RandomStringUtils.randomAlphanumeric(18));
        parameters.put("notify_url", weiXinPayConfiger.getCallBackUrl());
        String sign = createSign(parameters);
        parameters.put("sign", sign);
        RestTemplate template = new RestTemplate();
        String body = parseToXmlString(parameters);
        String resultStr = template.postForObject(WeiXinPayConfiger.ORDER_QUERY, body, String.class);
        return resultStr;
    }


    private String parseToXmlString(Map<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (Map.Entry<Object, Object> entry : parameters.entrySet()) {
            sb.append("<" + entry.getKey() + "><![CDATA[" + entry.getValue() + "]]></" + entry.getKey() + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    @SuppressWarnings("unchecked")
    public String createSign(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();// 所有参与传参的参数按照accsii排序（升序）
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + weiXinPayConfiger.getSignKey());
        String sign = MD5Util.MD5Encode(sb.toString()).toUpperCase();
        return sign;
    }

    public String getNodeInfo(String xml, String node) throws JDOMException, IOException {
        StringReader read = new StringReader(xml);
        InputSource source = new InputSource(read);
        SAXBuilder sb = new SAXBuilder();
        Document doc = sb.build(source);
        Element root = doc.getRootElement();
        Element ele = root.getChild(node);
        if (null == ele) {
            return null;
        }
        return ele.getValue();
    }
}
