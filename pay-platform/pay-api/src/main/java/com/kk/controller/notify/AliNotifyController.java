package com.kk.controller.notify;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.kk.api.service.PayService;
import com.kk.api.service.RefundService;
import com.kk.platform.enums.PayTypeCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝支付成功后 回调接口
 * <p/>
 * 1.必须保证服务器异步通知页面（notify_url）上无任何字符，如空格、HTML标签、开发系统自带抛出的异常提示信息等；
 * 2.支付宝是用POST方式发送通知信息，因此该页面中获取参数的方式，如：request.Form(“out_trade_no”)、$_POST[‘out_trade_no’]；
 * 3.支付宝主动发起通知，该方式才会被启用；
 * 4.只有在支付宝的交易管理中存在该笔交易，且发生了交易状态的改变，支付宝才会通过该方式发起服务器通知（即时到账中交易状态为“等待买家付款”的状态默认是不会发送通知的）；
 * 5.服务器间的交互，不像页面跳转同步通知可以在页面上显示出来，这种交互方式是不可见的；
 * 第一次交易状态改变（即时到账中此时交易状态是交易完成）时，不仅会返回同步处理结果，而且服务器异步通知页面也会收到支付宝发来的处理结果通知；
 * 6.程序执行完后必须打印输出“success”（不包含引号）。如果商户反馈给支付宝的字符不是success这7个字符，支付宝服务器会不断重发通知，直到超过24小时22分钟。一般情况下，25小时以内完成8次通知（通知的间隔频率一般是：4m,10m,10m,1h,2h,6h,15h）；
 * 7.程序执行完成后，该页面不能执行页面跳转。如果执行页面跳转，支付宝会收不到success字符，会被支付宝服务器判定为该页面程序运行出现异常，而重发处理结果通知；
 * 8.cookies、session等在此页面会失效，即无法获取这些数据；
 * 9.该方式的调试与运行必须在服务器上，即互联网上能访问；
 * 10.该方式的作用主要防止订单丢失，即页面跳转同步通知没有处理订单更新，它则去处理；
 * 11.当商户收到服务器异步通知并打印出success时，服务器异步通知参数notify_id才会失效。也就是说在支付宝发送同一条异步通知时（包含商户并未成功打印出success导致支付宝重发数次通知），服务器异步通知参数notify_id是不变的。
 * <p/>
 * <p/>
 * 签名规则：
 * 1.在通知返回参数列表中，除去sign、sign_type两个参数外，凡是通知返回回来的参数皆是待验签的参数。
 * 2.将剩下参数进行url_decode, 然后进行字典排序，组成字符串，得到待签名字符串：
 * 3.将签名参数（sign）使用base64解码为字节码串。
 * 4.使用RSA的验签方法，通过签名字符串、签名参数（经过base64解码）及支付宝公钥验证签名。
 * 5.商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
 * 同时需要校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
 * 上述有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，
 * 并且过滤重复的通知结果数据。在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
 */
@RestController
@RequestMapping("/notify/ali")
public class AliNotifyController {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PayService payService;

    @Autowired
    RefundService refundService;

    // ali支付成功 异步通知 结构为:key1=valu1&key2=val2 格式，  签名：sign_type=RSA，sign字段
    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object receiveNotify(HttpServletRequest request) {
        try {
            String data = getRequestData(request); // data json格式
            logger.info("ali pay notify result is：" + data);

            payService.handlePayNotify(PayTypeCode.ALI_PAY, data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "success";

    }

    /**
     * 即时到账有密退款接口，异步通知url
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/refund", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public Object receiveRefundNotify(HttpServletRequest request) {
        try {
            String data = getRequestData(request); // data json格式
            logger.info("ali pay notify result is：" + data);

            refundService.handleRefundNotify(PayTypeCode.ALI_PAY, data);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return "success";
    }

    /**
     * 转为json格式数据
     *
     * @param request
     * @return
     */
    public String getRequestData(HttpServletRequest request) {
        try {
            Map requestParams = request.getParameterMap();
            Map<String, String> responseData = new HashMap<String, String>();
            for (Iterator iterator = requestParams.keySet().iterator(); iterator.hasNext(); ) {
                String name = (String) iterator.next();
                String[] valueArray = (String[]) requestParams.get(name);
                String values = "";
                for (int i = 0; i < valueArray.length; i++) {
                    values = (i == valueArray.length - 1) ? values + valueArray[i]
                            : values + valueArray[i] + ",";
                }
                //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                //values = new String(values.getBytes("ISO-8859-1"), "gbk");
                responseData.put(name, values);
            }
            return new Gson().toJson(responseData);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
