package com.kk.controller;

import com.alibaba.fastjson.JSON;
import com.kk.api.ApiResponse;
import com.kk.api.request.PayRequest;
import com.kk.api.response.PayResponse;
import com.kk.platform.enums.PayTypeCode;
import com.kk.platform.enums.TradeTypeCode;
import com.kk.util.BeanUtil;
import com.kk.util.Consts;
import com.kk.util.DateUtil;
import com.kk.util.SignUtils;
import com.kk.utils.HttpClientUtil;
import com.kk.utils.JsonUtil;
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
import java.util.Map;

/**
 * 在微信浏览器中，h5页面调起
 * <p/>
 * 微信支付 收银台链接 必须以 '/' 结尾，   链接需要在微信公众平台配置（也以'/'结尾）， 线上和测试都需要配置；
 * 测试环境需要把测试白名单加到白名单中。
 * <p/>
 * 微信支付的时候需要openId， 需要和支付人的openId一致。
 */
@Controller
@RequestMapping("/h5/pay")
public class WeChatH5PayController {

    private Log logger = LogFactory.getLog(this.getClass());

    @RequestMapping(value = "cashier", method = RequestMethod.GET)
    public String cashier(HttpServletRequest request,
                          HttpServletResponse response, Model model) {

        String x = DateUtil.format(new Date(), new SimpleDateFormat("yyyyMMddHHmmss"));
        String voucherId = "test_kk_" + x;

        int total = 1;// 1分钱

        model.addAttribute("voucherId", voucherId);
        model.addAttribute("total", total);

        String returnUrl = Consts.domain_caller + "/h5/pay/success";
        model.addAttribute("returnUrl", returnUrl);

        return "weixin/pay";
    }

    @RequestMapping(value = "success", method = RequestMethod.GET)
    @ResponseBody
    public String success(HttpServletRequest request,
                          HttpServletResponse response, Model model) {
        return "success";
    }

    @RequestMapping(value = "do", method = RequestMethod.POST, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String payDo(Model model,
                        @RequestParam("voucherId") String voucherId,
                        @RequestParam("total") int total) {
        String url = Consts.domain + "/pay/request";

        PayRequest request = new PayRequest();
        request.setMerchantId(Consts.mchId);
        request.setTradePayNo(voucherId);
        request.setPayAmount(total);
        request.setPayType(PayTypeCode.WECHAT_PAY);

        request.setTradeType(TradeTypeCode.WAP); //!!

        request.setSubject("kk网页测试标题");
        request.setDetail("kk网页测试详情");
        // 支付成功后回调url
        request.setNotifyUrl(Consts.notifyUrlWeixin);
        request.setExtra("extra.env=test");

        request.setOpenId(Consts.openId);// !!

        request.validate();

        // 生成签名
        Map<String, String> data = BeanUtil.object2Map(request);
        String sign = SignUtils.md5(data, Consts.apiKey);

        request.setSign(sign);


        String ret = HttpClientUtil.postData(url, request.toJsonString());

        logger.info("ret=" + ret);

        PayResponse response = JSON.parseObject(ret, PayResponse.class);

        logger.info(JSON.toJSONString(response));

        logger.info(PayResponse.isSuccess(response.getCode()));

        // 校验签名
        if (ApiResponse.isSuccess(response.getCode())) {
            data = BeanUtil.object2Map(response);
            sign = SignUtils.md5(data, Consts.apiKey);
            if (!sign.equals(response.getSign())) {
                logger.info("签名不正确,sign=" + sign);
            }
        }
        // 推荐使用json方式来校验签名
        if (ApiResponse.isSuccess(response.getCode())) {
            data = BeanUtil.jsonStr2Map(ret);
            sign = SignUtils.md5(data, Consts.apiKey);
            if (!sign.equals(response.getSign())) {
                logger.info("json签名不正确,sign=" + sign);
            } else {
                logger.info("使用json来校验签名，签名正确");
            }
        }

        net.sf.json.JSONObject result = JsonUtil.getOkJson();
        result.put("param", response.getCredential());
        return result.toString();
    }
}
