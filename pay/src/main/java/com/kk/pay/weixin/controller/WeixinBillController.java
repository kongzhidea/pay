package com.kk.pay.weixin.controller;

import com.kk.pay.util.*;
import com.kk.pay.weixin.model.PayBillParam;
import com.kk.pay.weixin.model.enums.PayBillField;
import com.kk.pay.weixin.model.enums.PayOrderField;
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
 * 下载对账单
 */
@Controller
@RequestMapping("/kk/weixin/bill")
public class WeixinBillController {

    private static Log logger = LogFactory.getLog(WeixinBillController.class);

    // pay 微信支付商户号
    String mchId = WebPropertiesUtil.getInstance().getValue("weixin.mchid");

    //mp.weixin  微信公共账号 appId
    String appId = WebPropertiesUtil.getInstance().getValue("weixin.appid");

    // 申请开通微信支付后，发给开发者。用于计算签名
    String apiKey = WebPropertiesUtil.getInstance().getValue("weixin.pay.apikey");

    String billUrl = WebPropertiesUtil.getInstance().getValue("weixin.pay.bill");

    @RequestMapping(value = "", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String bill(HttpServletRequest request,
                       HttpServletResponse response,
                       @RequestParam(value = "bill_date", required = false) String bill_date,
                       @RequestParam(value = "bill_type", required = false) String bill_type) {

        PayBillParam param = new PayBillParam();
        // 基本信息
        param.setAppid(appId);
        param.setMchId(mchId);

        //业务信息
        param.setBillDate(bill_date);
        param.setBill_type(bill_type);


        //签名
        param.setNonceStr(EncryptUtil.random());
        Map<String, Object> data = BeanUtil.object2Map(param); // 参数列表
        param.setSign(SignUtil.sign(data, apiKey)); // 计算sign
        data.put(PayOrderField.SIGN.getField(), param.getSign()); // sign放到map中，为后续转xml

        // 校验参数是否齐全
        ValidateUtil.validate(PayBillField.values(), data);

        // 转成xml格式
        String xml = XmlUtil.toXml(data);
        logger.info("post.xml=" + xml);
        // 发送支付请求
        String resultStr = WeixinUtil.postXml(billUrl, xml);
        logger.info("result=" + resultStr);

        return resultStr;
    }

}
