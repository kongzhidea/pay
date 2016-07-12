package com.kk.pay.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.kk.pay.util.*;
import com.kk.pay.weixin.model.PayQueryParam;
import com.kk.pay.weixin.model.PayQueryResult;
import com.kk.pay.weixin.model.enums.PayOrderField;
import com.kk.pay.weixin.model.enums.PayQueryField;
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
 * 查询订单
 */
@Controller
@RequestMapping("/kk/weixin/query")
public class WeixinQueryPayController {
    private static Log logger = LogFactory.getLog(WeixinQueryPayController.class);

    // pay 微信支付商户号
    String mchId = WebPropertiesUtil.getInstance().getValue("weixin.mchid");

    //mp.weixin  微信公共账号 appId
    String appId = WebPropertiesUtil.getInstance().getValue("weixin.appid");

    // 申请开通微信支付后，发给开发者。用于计算签名
    String apiKey = WebPropertiesUtil.getInstance().getValue("weixin.pay.apikey");

    String orderQueryUrl = WebPropertiesUtil.getInstance().getValue("weixin.pay.orderquery");


    @RequestMapping(value = "", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String query(HttpServletRequest request,
                             HttpServletResponse response,
                             @RequestParam(value = "wxid", required = false) String wxid,
                             @RequestParam(value = "voucherId", required = false) String voucherId) {

        PayQueryParam param = new PayQueryParam();
        // 基本信息
        param.setAppid(appId);
        param.setMchId(mchId);

        param.setOutTradeNo(voucherId); // 客户订单号
        param.setTransactionId(wxid); //微信订单号

        //签名
        param.setNonceStr(EncryptUtil.random());
        Map<String, Object> data = BeanUtil.object2Map(param); // 参数列表
        param.setSign(SignUtil.sign(data, apiKey)); // 计算sign
        data.put(PayOrderField.SIGN.getField(), param.getSign()); // sign放到map中，为后续转xml

        // 校验参数是否齐全
        ValidateUtil.validate(PayQueryField.values(), data);

        // 转成xml格式
        String xml = XmlUtil.toXml(data);
        logger.info("post.xml=" + xml);
        // 发送支付请求
        String resultStr = WeixinUtil.postXml(orderQueryUrl, xml);
        logger.info("result=" + resultStr);

        // 校验返回结果 签名
        Map<String, Object> resultMap = XmlUtil.parseXml(resultStr);
        String resultSign = SignUtil.sign(resultMap, apiKey);
        if (resultMap.get("sign") == null || !resultMap.get("sign").equals(resultSign)) {
            logger.info("sign is not correct, " + resultMap.get("sign") + " " + resultSign);
            throw new RuntimeException("签名校验不通过");
        }

        PayQueryResult result = BeanUtil.map2Object(PayQueryResult.class, resultMap);

        return JSON.toJSONString(result, true);
    }

}
