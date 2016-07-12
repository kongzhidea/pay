package com.kk.pay.weixin.controller;

import com.alibaba.fastjson.JSON;
import com.kk.pay.util.*;
import com.kk.pay.weixin.model.PayRefundParam;
import com.kk.pay.weixin.model.PayRefundQueryParam;
import com.kk.pay.weixin.model.PayRefundQueryResult;
import com.kk.pay.weixin.model.PayRefundResult;
import com.kk.pay.weixin.model.enums.PayOrderField;
import com.kk.pay.weixin.model.enums.PayRefundField;
import com.kk.pay.weixin.model.enums.PayRefundQueryField;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * 微信退款， 需要安装证书(apiclient_cert.p12)
 */
@Controller
@RequestMapping("/kk/weixin/refund")
public class WeixinRefundPayController {
    private static Log logger = LogFactory.getLog(WeixinRefundPayController.class);

    // pay 微信支付商户号
    String mchId = WebPropertiesUtil.getInstance().getValue("weixin.mchid");

    //mp.weixin  微信公共账号 appId
    String appId = WebPropertiesUtil.getInstance().getValue("weixin.appid");

    // 申请开通微信支付后，发给开发者。用于计算签名
    String apiKey = WebPropertiesUtil.getInstance().getValue("weixin.pay.apikey");

    String refundUrl = WebPropertiesUtil.getInstance().getValue("weixin.pay.refund");

    String refundqueryUrl = WebPropertiesUtil.getInstance().getValue("weixin.pay.refundquery");


    @RequestMapping(value = "", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String refund(HttpServletRequest request,
                         HttpServletResponse response,
                         @RequestParam(value = "wxid", required = false) String wxid,
                         @RequestParam(value = "voucherId", required = false) String voucherId,
                         @RequestParam(value = "outRefundNo") String outRefundNo
    ) {
        try {
            PayRefundParam param = new PayRefundParam();
            // 基本信息
            param.setAppid(appId);
            param.setMchId(mchId);

            param.setOutTradeNo(voucherId); // 客户订单号
            param.setTransactionId(wxid); //微信订单号


            // 业务信息
            param.setOutRefundNo(outRefundNo);
            param.setTotalFee(1); // 总金额
            param.setRefundFee(1); // 退款总金额
            param.setOpUserId(mchId);

            //签名
            param.setNonceStr(EncryptUtil.random());
            Map<String, Object> data = BeanUtil.object2Map(param); // 参数列表
            param.setSign(SignUtil.sign(data, apiKey)); // 计算sign
            data.put(PayOrderField.SIGN.getField(), param.getSign()); // sign放到map中，为后续转xml

            // 校验参数是否齐全
            ValidateUtil.validate(PayRefundField.values(), data);

            // 转成xml格式
            String xml = XmlUtil.toXml(data);
            logger.info("post.xml=" + xml);
            // 发送支付请求
            FileInputStream in = new FileInputStream(new File("/data/zhihui.kong/tomcat/baks/ap2"));
            String resultStr = WeixinUtil.postXmlWithKey(refundUrl, xml, in, mchId);
            logger.info("result=" + resultStr);

            // 校验返回结果 签名
            Map<String, Object> resultMap = XmlUtil.parseXml(resultStr);
            String resultSign = SignUtil.sign(resultMap, apiKey);
            if (resultMap.get("sign") == null || !resultMap.get("sign").equals(resultSign)) {
                logger.info("sign is not correct, " + resultMap.get("sign") + " " + resultSign);
                throw new RuntimeException("签名校验不通过");
            }

            PayRefundResult result = BeanUtil.map2Object(PayRefundResult.class, resultMap);

            return JSON.toJSONString(result);
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    @RequestMapping(value = "query", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String refundQuery(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam(value = "wxid", required = false) String wxid,
                              @RequestParam(value = "voucherId", required = false) String voucherId,
                              @RequestParam(value = "outRefundNo", required = false) String outRefundNo,
                              @RequestParam(value = "refundId", required = false) String refundId
    ) {

        PayRefundQueryParam param = new PayRefundQueryParam();
        // 基本信息
        param.setAppid(appId);
        param.setMchId(mchId);

        param.setOutTradeNo(voucherId); // 客户订单号
        param.setTransactionId(wxid); //微信订单号


        // 业务信息
        param.setOutRefundNo(outRefundNo);
        param.setRefundId(refundId);

        //签名
        param.setNonceStr(EncryptUtil.random());
        Map<String, Object> data = BeanUtil.object2Map(param); // 参数列表
        param.setSign(SignUtil.sign(data, apiKey)); // 计算sign
        data.put(PayOrderField.SIGN.getField(), param.getSign()); // sign放到map中，为后续转xml

        // 校验参数是否齐全
        ValidateUtil.validate(PayRefundQueryField.values(), data);

        // 转成xml格式
        String xml = XmlUtil.toXml(data);
        logger.info("post.xml=" + xml);
        // 发送支付请求
        String resultStr = WeixinUtil.postXml(refundqueryUrl, xml);

        logger.info("result=" + resultStr);

        // 校验返回结果 签名
        Map<String, Object> resultMap = XmlUtil.parseXml(resultStr);
        String resultSign = SignUtil.sign(resultMap, apiKey);
        if (resultMap.get("sign") == null || !resultMap.get("sign").equals(resultSign)) {
            logger.info("sign is not correct, " + resultMap.get("sign") + " " + resultSign);
            throw new RuntimeException("签名校验不通过");
        }

        PayRefundQueryResult result = BeanUtil.map2Object(PayRefundQueryResult.class, resultMap);
        result.setResultMap(resultMap);

        return JSON.toJSONString(result, true);
    }
}
