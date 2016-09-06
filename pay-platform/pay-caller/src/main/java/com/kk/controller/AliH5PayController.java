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
 * 支付宝网页支付
 * <p/>
 * 在微信中不能直接使用，需要调用pay.js 中转一下。
 */
@Controller
@RequestMapping("/caller/h5/alipay")
public class AliH5PayController {

    private Log logger = LogFactory.getLog(this.getClass());

    // 在微信中 使用支付宝 需要加这一段,相对cashier链接,  链接相对  h5pay,  所以h5pay需要以  / 结尾。
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

        return "alipay/pay";
    }

    // 在微信中 使用支付宝 需要加这一段,相对cashier链接,  链接相对  h5pay,  所以h5pay需要以  / 结尾。
    @RequestMapping(value = {"cashier/pay.htm", "pay.htm"}, method = RequestMethod.GET)
    public String cashier(HttpServletRequest request, Model model) {
        return "alipay/alipay";
    }

    /**
     * 设置支付成功，以异步的通知为准，  同步通知仅作为成功页展示
     *
     * @param request
     * @param response
     * @param model
     * @return
     */
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
        request.setPayType(PayTypeCode.ALI_PAY);

        String returnUrl = Consts.domain_caller + "/caller/h5/alipay/success";
        request.setReturnUrl(returnUrl);

        request.setTradeType(TradeTypeCode.WAP); //!!

        request.setSubject("kk网页测试标题");
        request.setDetail("kk网页测试详情");
        // 支付成功后回调url
        request.setNotifyUrl(Consts.notifyUrlWeixin);
        request.setExtra("extra.env=test");

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
        result.put("params", response.getCredential());
        return result.toString();
    }
}
