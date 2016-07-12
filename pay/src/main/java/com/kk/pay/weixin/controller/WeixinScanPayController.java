package com.kk.pay.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.kk.pay.base.model.PayResult;
import com.kk.pay.base.model.enums.ResultCode;
import com.kk.pay.util.*;
import com.kk.pay.weixin.model.PayNotifyResult;
import com.kk.pay.weixin.model.PayOrderParam;
import com.kk.pay.weixin.model.PayOrderResult;
import com.kk.pay.weixin.model.enums.PayOrderField;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 扫码支付
 */
@Controller
@RequestMapping("/kk/weixin/scan")
public class WeixinScanPayController {
    private static Log logger = LogFactory.getLog(WeixinScanPayController.class);

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


    @RequestMapping(value = "", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String pay(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam("voucherId") String voucherId) {

        PayOrderParam param = new PayOrderParam();
        // 基本信息
        param.setAppid(appId);
        param.setMchId(mchId);
        param.setTradeType("NATIVE"); // 扫码支付
        param.setSpbillCreateIp(LocalIPUtil.getLocalAddr());
        //param.setLimitPay("no_credit"); // 禁止用信用卡

        param.setNotifyUrl(notifyUrl);//回调url

        // 业务相关参数
        JSONObject atach = new JSONObject();
        atach.put("order_id", 11);
        param.setAttach(atach.toString());
        param.setBody("支付测试订单");
        param.setTotalFee(1); // 1分钱
        param.setOutTradeNo(voucherId); // 客户订单号

        //签名
        param.setNonceStr(EncryptUtil.random());
        Map<String, Object> data = BeanUtil.object2Map(param); // 参数列表
        param.setSign(SignUtil.sign(data, apiKey)); // 计算sign
        data.put(PayOrderField.SIGN.getField(), param.getSign()); // sign放到map中，为后续转xml

        // 校验参数是否齐全
        ValidateUtil.validate(PayOrderField.values(), data);

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

        return JSON.toJSONString(payResult);
    }


    // 回调url
    @RequestMapping(value = "notify", produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String notify(HttpServletRequest request) {
        try {
            String xml = IOUtils.toString(request.getInputStream());
            logger.info("weixin pay notify result is " + xml);
            if (StringUtils.isBlank(xml)) {
                return WeixinUtil.getResult(ResultCode.FAIL, "接受参数为空");
            }

            Map<String, Object> map = XmlUtil.parseXml(xml);

            PayNotifyResult result = BeanUtil.map2Object(PayNotifyResult.class, map);
            logger.info(JSON.toJSONString(result, true));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return WeixinUtil.getResult(ResultCode.FAIL, "解析异常");
        }
        return WeixinUtil.getResult(ResultCode.SUCCESS, "OK");
    }

}
