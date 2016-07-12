package com.kk.pay.alipay.controller;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.config.AlipayConfig;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.ExtendParams;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateContentBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.alipay.util.AlipayNotify;
import com.alipay.util.AlipaySubmit;
import com.kk.pay.util.DateUtil;
import com.kk.pay.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 网页支付
 * <p/>
 * https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.7WFEkV&treeId=60&articleId=103564&docType=1
 */
@Controller
@RequestMapping("/kk/alipay/h5pay")
public class AlipayH5Controller {

    private Log logger = LogFactory.getLog(AlipayH5Controller.class);


    // http://www.kk.com/kk/alipay/h5pay/  测试地址
    @RequestMapping(value = "", method = {RequestMethod.GET})
    public String pay(HttpServletRequest request, Model model) {
        return "alipay/pay";
    }

    // 在微信中 使用支付宝 需要加这一段,相对cashier链接,  链接相对  h5pay,  所以h5pay需要以  / 结尾。
    @RequestMapping(value = "pay.htm", method = RequestMethod.GET)
    public String cashier(HttpServletRequest request, Model model) {
        return "alipay/alipay";
    }


    // 预下单 生成 支付宝对应的参数返回到前端， 前端控制页面跳转
    @RequestMapping(value = "api", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String api(HttpServletRequest request, Model model,
                      //商户订单号，商户网站订单系统中唯一订单号，必填
                      @RequestParam("WIDout_trade_no") String out_trade_no,
                      //订单名称，必填
                      @RequestParam("WIDsubject") String subject,
                      //付款金额，必填
                      @RequestParam("WIDtotal_fee") String total_fee,
                      //收银台页面上，商品展示的超链接，必填
                      @RequestParam("WIDshow_url") String show_url,
                      //商品描述，可空
                      @RequestParam("WIDbody") String body
    ) {

        //把请求参数打包成数组
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", AlipayConfig.notify_url);
        sParaTemp.put("return_url", AlipayConfig.return_url);
        sParaTemp.put("out_trade_no", out_trade_no);
        sParaTemp.put("subject", subject);
        sParaTemp.put("total_fee", total_fee);
        sParaTemp.put("show_url", show_url);
        sParaTemp.put("body", body);
        //其他业务参数根据在线开发文档，添加参数.文档地址:https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.2Z6TSk&treeId=60&articleId=103693&docType=1
        //如sParaTemp.put("参数名","参数值");


        //建立请求， 生成跳转页面代码，  非Ajax场景使用
        //String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "get", "确认");


        // 计算签名， 由前端控制跳转   ajax方式
        Map<String, String> sPara = AlipaySubmit.buildRequestPara(sParaTemp);

        JSONObject ret = JsonUtil.getOkJson();
        ret.put("params", sPara);
        return ret.toString();
    }

    // return_url  支付完成后跳转页面
    @RequestMapping(value = "return", method = RequestMethod.GET, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String return_url(HttpServletRequest request, Model model) {

        //获取支付宝GET过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
//            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        for (String key : params.keySet()) {
            logger.info(key + "=" + params.get(key));
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号
        String out_trade_no = request.getParameter("out_trade_no");

        //支付宝交易号
        String trade_no = request.getParameter("trade_no");

        //交易状态
        String trade_status = request.getParameter("trade_status");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//

        //计算得出通知验证结果
        boolean verify_result = AlipayNotify.verify(params);

        if (verify_result) {//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
            if (trade_status.equals("TRADE_FINISHED") || trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //如果有做过处理，不执行商户的业务程序
            }

            logger.info("签名成功");
            //该页面可做页面美工编辑
            return "验证成功";
            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——

            //////////////////////////////////////////////////////////////////////////////////////////
        } else {
            //该页面可做页面美工编辑
            return "验证失败";
        }

    }


    // notify_url,  异步通知
    @RequestMapping(value = "notify", method = {RequestMethod.GET,
            RequestMethod.POST}, produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public String notify(HttpServletRequest request, Model model) {

        //获取支付宝POST过来反馈信息
        Map<String, String> params = new HashMap<String, String>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext(); ) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }


        for (String key : params.keySet()) {
            logger.info(key + "=" + params.get(key));
        }

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//
        //商户订单号
        String out_trade_no = request.getParameter("out_trade_no");

        //支付宝交易号
        String trade_no = request.getParameter("trade_no");

        //交易状态
        String trade_status = request.getParameter("trade_status");

        //获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以上仅供参考)//
        if (AlipayNotify.verify(params)) {//验证成功
            //////////////////////////////////////////////////////////////////////////////////////////
            //请在这里加上商户的业务逻辑程序代码

            //——请根据您的业务逻辑来编写程序（以下代码仅作参考）——

            if (trade_status.equals("TRADE_FINISHED")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
            } else if (trade_status.equals("TRADE_SUCCESS")) {
                //判断该笔订单是否在商户网站中已经做过处理
                //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                //如果有做过处理，不执行商户的业务程序

                //注意：
                //付款完成后，支付宝系统发送该交易状态通知
            }

            //——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
            logger.info("签名成功");
            return "success";    //请不要修改或删除

            //////////////////////////////////////////////////////////////////////////////////////////
        } else {//验证失败
            return "fail";
        }
    }
}
