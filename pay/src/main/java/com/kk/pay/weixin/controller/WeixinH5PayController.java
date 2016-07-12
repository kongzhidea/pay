package com.kk.pay.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kk.pay.base.model.PayResult;
import com.kk.pay.base.model.enums.ResultCode;
import com.kk.pay.util.*;
import com.kk.pay.weixin.model.PayOrderParam;
import com.kk.pay.weixin.model.PayOrderResult;
import com.kk.pay.weixin.model.enums.PayOrderField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 在微信浏览器中，h5页面调起
 */
@Controller
@RequestMapping("/kk/weixin/h5pay")
public class WeixinH5PayController {

    private static Log logger = LogFactory.getLog(WeixinH5PayController.class);

    // pay 微信支付商户号
    String mchId = WebPropertiesUtil.getInstance().getValue("weixin.mchid");

    //mp.weixin  微信公共账号 appId
    String appId = WebPropertiesUtil.getInstance().getValue("weixin.appid");

    // 申请开通微信支付后，发给开发者。用于计算签名
    String apiKey = WebPropertiesUtil.getInstance().getValue("weixin.pay.apikey");

    // 微信支付 统一下单接口
    String payOrderUrl = WebPropertiesUtil.getInstance().getValue("weixin.pay.unifiedorder");

    // 支付 回调url
    String notifyUrl = WebPropertiesUtil.getInstance().getValue("server.url") + "/kk/weixin/scan/notify";

    /**
     * 测试 微信openId  kk
     * <p/>
     * voucherId  客户订单号
     */
    String openId = "";

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String pay(HttpServletRequest request,
                      HttpServletResponse response, Model model) {

        String x = DateUtil.format(new Date(), new SimpleDateFormat("yyyyMMddHHmmss"));
        String voucherId = "test_kk_" + x;

        int total = 1;// 1分钱

        model.addAttribute("voucherId", voucherId);
        model.addAttribute("total", total);

        return "weixin/pay";
    }

    @RequestMapping(value = "do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String payDo(HttpServletRequest request,
                        HttpServletResponse response, Model model,
                        @RequestParam("voucherId") String voucherId,
                        @RequestParam("total") int total) {


        PayOrderParam param = new PayOrderParam();
        // 基本信息
        param.setAppid(appId);
        param.setMchId(mchId);
        param.setTradeType("JSAPI"); // 公众号支付
        param.setOpenid(openId); // openId!!
        param.setSpbillCreateIp(LocalIPUtil.getLocalAddr());
        //param.setLimitPay("no_credit"); // 禁止用信用卡

        param.setNotifyUrl(notifyUrl); // 支付成功回调url

        // 业务相关参数
        JSONObject atach = new JSONObject();
        atach.put("order_id", 11);
        param.setAttach(atach.toString());
        param.setBody("支付测试订单");
        param.setTotalFee(total);
        param.setOutTradeNo(voucherId); // 客户订单号

        //签名
        param.setNonceStr(EncryptUtil.random());
        Map<String, Object> data = BeanUtil.object2Map(param); // 参数列表
        param.setSign(SignUtil.sign(data, apiKey)); // 计算sign
        data.put(PayOrderField.SIGN.getField(), param.getSign()); // sign放到map中，为后续转xml

        // 校验参数是否齐全
        try {
            ValidateUtil.validate(PayOrderField.values(), data);
        } catch (Exception e) {
            return JsonUtil.getJson(1, e.getMessage()).toString();
        }

        // 转成xml格式
        String xml = XmlUtil.toXml(data);
        logger.info("post.xml=" + xml);
        // 发送支付请求
        String resultStr = WeixinUtil.postXml(payOrderUrl, xml);
        logger.info("result=" + resultStr);

        // 校验返回结果 签名
        Map<String, Object> resultMap = XmlUtil.parseXml(resultStr);
        String resultSign = SignUtil.sign(resultMap, apiKey);
        if (resultMap.get("sign") == null || !resultMap.get("sign").equals(resultSign)) {
            logger.info("sign is not correct, " + resultMap.get("sign") + " " + resultSign);
            throw new RuntimeException("签名校验不通过");
        }

        PayOrderResult result = BeanUtil.map2Object(PayOrderResult.class, resultMap);

        PayResult payResult = new PayResult();
        if (ResultCode.SUCCESS.getCode().equals(result.getReturnCode())
                && ResultCode.SUCCESS.getCode().equals(result.getResultCode())) {
            payResult.setResultCode(ResultCode.SUCCESS.getCode());
        } else {
            payResult.setResultCode(ResultCode.FAIL.getCode());
        }
        payResult.setMessage(result.getReturnMsg());
        payResult.setErrCode(result.getErrCode());
        payResult.setErrorMessage(result.getErrCodeDes());
        payResult.setPrepayId(result.getPrepayId());
        payResult.setCodeUrl(result.getCodeUrl());

        JSONObject ret = JsonUtil.getOkJson();
        ret.put("reslt", payResult);
        ret.put("param", genJsParam(payResult));

        return ret.toString();
    }

    private JSONObject genJsParam(PayResult payResult) {
        long timestamp = System.currentTimeMillis() / 1000;
        String nonceStr = EncryptUtil.random();

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("appId", appId);
        data.put("timeStamp", timestamp);
        data.put("nonceStr", nonceStr);
        data.put("package", "prepay_id=" + payResult.getPrepayId());
        data.put("signType", "MD5");

        data.put("paySign", SignUtil.sign(data, apiKey)); // 计算sign

        JSONObject ret = JSONObject.parseObject(JSON.toJSONString(data));
        return ret;
    }
}
