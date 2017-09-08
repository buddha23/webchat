package com.lld360.alipay.bean;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.lld360.alipay.AlipayConfig;
import com.lld360.alipay.util.AlipaySignUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class AlipayTradeBuilder {
    private static final String KEY_PARTNER = "partner";//签约合作者身份ID
    private static final String KEY_SELLER_ID = "seller_id";//签约卖家支付宝账号
    public static final String KEY_OUT_TRADE_NO = "out_trade_no";//商户网站唯一订单号
    private static final String KEY_SUBJECT = "subject";//商品名称
    public static final String KEY_BODY = "body";//商品详情
    public static final String KEY_TOTAL_FEE = "total_fee";//商品金额
    public static final String KEY_TOTAL_AMOUNT = "total_amount";//商品金额
    private static final String KEY_NOTIFY_URL = "notify_url";//服务器异步通知页面路径
    private static final String KEY_RETURN_URL = "return_url";//页面跳转同步通知页面路径
    private static final String KEY_SERVICE = "service";//服务接口名称， 固定值
    private static final String KEY_PAYMENT_TYPE = "payment_type";//支付类型， 固定值
    private static final String KEY_INPUT_CHARSET = "_input_charset";//参数编码， 固定值
    private static final String KEY_IT_B_PAY = "it_b_pay";//交易的超时时间：默认30m，超时自动关闭。1m～15d，m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭），只能穿整数
    public static final String KEY_SIGN = "sign";// 签名信息
    public static final String KEY_SIGN_TYPE = "sign_type";// 签名类型，仅支持RSA
    private static final String KEY_GOODS_TYPE = "goods_type";//扩展：商品类型,"1":实物交易,"0":虚拟交易，默认"1"

    public static final String KEY_TRADE_NO = "trade_no";
    public static final String KEY_TRADE_STATUS = "trade_status";

    private AlipayConfig configer;

    public AlipayTradeBuilder(AlipayConfig configer) {
        this.configer = configer;
    }

    /**
     * 生成订单信息+签名
     *
     * @param orderName
     * @param orderDetail
     * @param orderNo
     * @param money
     * @return
     */
    public Map<String, Object> buildTradeByWeb(String orderName, String orderDetail, String orderNo, BigDecimal money) {
        Map<String, Object> map = buildMap(orderName, orderDetail, orderNo, getMoney(money, 2));
        String sign = getSign(map);
        if (StringUtils.isEmpty(sign)) return null;
        map.put(KEY_SIGN, sign);
        map.put(KEY_SIGN_TYPE, AlipayConfig.SIGN_TYPE);
        return map;
    }

    /**
     * 获取 订单异步通知 数据
     *
     * @param request
     * @return
     */
    public Map<String, String> getAlipayNotifyParams(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        if (null == request) return params;
        requestParams.forEach((key, vlaue) -> params.put(key, null == vlaue ? null : String.join(",", vlaue)));
        return params;
    }

    public String buildTradeByWap(String orderName, String orderDetail, String orderNo, BigDecimal money) {
        AlipayClient alipayClient = new DefaultAlipayClient(AlipayConfig.WAP_GATEWAY, configer.getAlipayWapAppId(), configer.getAlipayWapPrivateKey(), AlipayConfig.WAP_FROM, AlipayConfig.INPUT_CHARSET, configer.getAlipayWapPublicKey());

        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        if (!StringUtils.isEmpty(configer.getAlipayWapReturnUrl())) {
            alipayRequest.setReturnUrl(configer.getAlipayWapReturnUrl());
        }
        alipayRequest.setNotifyUrl(configer.getAlipayWapNotifyUrl());//在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent("{" +
                "\"out_trade_no\":\"" + orderNo + "\"," +
                "\"total_amount\":" + getMoney(money, 2) + "," +
                "\"subject\":\"" + orderName + "\"," +
                "\"body\":\"" + orderDetail + "\"," +
                "\"seller_id\":\"" + configer.getAlipayPid() + "\"," +
                "\"product_code\":\"QUICK_WAP_PAY\"" +
                "  }");//填充业务参数
        try {
            return alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return null;
    }

    private double getMoney(BigDecimal b, int len) {
        BigDecimal b2 = new BigDecimal(1);
        return b.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    private String getSign(Map<String, Object> map) {
        StringBuffer infoStr = new StringBuffer();
        map.forEach((key, vlaue) -> infoStr.append("&").append(key).append("=").append(vlaue));
        infoStr.delete(0, 1);
        String signStr = AlipaySignUtils.sign(infoStr.toString(), configer.getAlipayPrivateKey(), AlipayConfig.INPUT_CHARSET);// 对订单做RSA 签名
        try {
            return StringUtils.isEmpty(signStr) ? null : URLEncoder.encode(signStr, AlipayConfig.INPUT_CHARSET);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, Object> buildMap(String orderName, String orderDetail, String orderNo, double money) {
        Map<String, Object> map = new TreeMap<>();
        map.put(KEY_SERVICE, AlipayConfig.SERVICE);
        map.put(KEY_PARTNER, configer.getAlipayPid());
        map.put(KEY_SELLER_ID, configer.getAlipayPid());
        map.put(KEY_INPUT_CHARSET, AlipayConfig.INPUT_CHARSET);
        if (!StringUtils.isEmpty(configer.getAlipayNotifyUrl())) map.put(KEY_NOTIFY_URL, configer.getAlipayNotifyUrl());
        if (!StringUtils.isEmpty(configer.getAlipayReturnUrl())) map.put(KEY_RETURN_URL, configer.getAlipayReturnUrl());
        map.put(KEY_OUT_TRADE_NO, orderNo);
        map.put(KEY_SUBJECT, orderName);
        map.put(KEY_PAYMENT_TYPE, AlipayConfig.PAYMENT_TYPE);
        map.put(KEY_TOTAL_FEE, money);
        if (!StringUtils.isEmpty(orderDetail)) map.put(KEY_BODY, orderDetail);
        map.put(KEY_IT_B_PAY, AlipayConfig.PAY_TIME);
        map.put(KEY_GOODS_TYPE, AlipayConfig.GOODS_TYPE);
        return map;
    }

    //-------------验证签名
    /**
     * 支付宝消息验证地址
     */
    private static final String HTTPS_VERIFY_URL = "https://mapi.alipay.com/gateway.do?service=notify_verify&";

    /**
     * 验证消息是否是支付宝发出的合法消息
     *
     * @param params          支付宝通知数据
     * @param alipayPublicKey 支付宝公钥
     * @return
     */
    public boolean verify(Map<String, String> params, String alipayPublicKey) {
        //判断responsetTxt是否为true，isSign是否为true
        //responsetTxt的结果不是true，与服务器设置问题、合作身份者ID、notify_id一分钟失效有关
        //isSign不是true，与安全校验码、请求时的参数格式（如：带自定义参数等）、编码格式有关
        String responseTxt = "false";
        if (params.get("notify_id") != null) {
            String notify_id = params.get("notify_id");
            responseTxt = verifyResponse(notify_id, configer.getAlipayPid());
        }
        String sign = "";
        if (params.get("sign") != null) sign = params.get("sign");
        boolean isSign = getSignVeryfy(params, sign, alipayPublicKey, AlipayConfig.INPUT_CHARSET);
        return isSign && responseTxt.equals("true");
    }

    public Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new TreeMap<>();
        if (sArray == null || sArray.size() <= 0) return result;
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (StringUtils.isEmpty(value) || key.equalsIgnoreCase(KEY_SIGN) || key.equalsIgnoreCase(KEY_SIGN_TYPE)) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param Params 通知返回来的参数数组
     * @param sign   比对的签名结果
     * @return 生成的签名结果
     */
    private boolean getSignVeryfy(Map<String, String> Params, String sign, String publicKey, String inputCharset) {
        //过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = paraFilter(Params);
        StringBuffer infoStr = new StringBuffer();
        sParaNew.forEach((key, vlaue) -> infoStr.append("&").append(key).append("=").append(vlaue));
        infoStr.delete(0, 1);

        return AlipaySignUtils.verify(infoStr.toString(), sign, publicKey, inputCharset);
    }

    /**
     * 获取远程服务器ATN结果,验证返回URL
     *
     * @param notify_id 通知校验ID
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private String verifyResponse(String notify_id, String partner) {
        //获取远程服务器ATN结果，验证是否是支付宝服务器发来的请求
        String veryfy_url = HTTPS_VERIFY_URL + "partner=" + partner + "&notify_id=" + notify_id;

        return checkUrl(veryfy_url);
    }

    /**
     * 获取远程服务器ATN结果
     *
     * @param urlvalue 指定URL路径地址
     * @return 服务器ATN结果
     * 验证结果集：
     * invalid命令参数不对 出现这个错误，请检测返回处理中partner和key是否为空
     * true 返回正确信息
     * false 请检查防火墙或者是服务器阻止端口问题以及验证时间是否超过一分钟
     */
    private String checkUrl(String urlvalue) {
        String inputLine = "";
        try {
            URL url = new URL(urlvalue);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection
                    .getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
            inputLine = "";
        }
        return inputLine;
    }

    public boolean checkData(StringBuilder result, Map<String, String> params, String out_trade_no, String trade_no, String trade_status, String alipayPublicKey) {
        params.forEach((key, vlaue) -> result.append(key).append("=").append(vlaue).append(","));

        if (!StringUtils.isEmpty(out_trade_no) && !StringUtils.isEmpty(trade_no) && !StringUtils.isEmpty(trade_status)) {
            boolean verify = verify(params, alipayPublicKey);
            result.append("验证结果").append("=").append(verify);
            return verify;
        } else {
            result.append("验证结果:false, out_trade_no|trade_no|trade_status is null");
            return false;
        }
    }

    public boolean isTradeFinish(String trade_status) {
        return ("TRADE_FINISHED".equals(trade_status) || "TRADE_SUCCESS".equals(trade_status));
    }
}
