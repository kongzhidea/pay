package com.kk.controller.api;

import com.alibaba.fastjson.JSON;
import com.kk.api.ApiResponse;
import com.kk.api.request.PayQueryRequest;
import com.kk.api.request.PayRequest;
import com.kk.api.service.PayService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 统一下单
 * <p/>
 * 字段为空 不参加计算签名。
 * <p/>
 * ResponseBody，如果返回object，则使用jackson， 字段为空也会解析到json中，
 * 如果返回 response.toJsonString，则使用fastjson解析， 如果字段为空或者null，则解析json的时候 过滤掉此字段。
 * 因为 字段为空 不参加计算签名，所以两种方式都可以，但是response.toJsonString对Date类型做了处理。
 * <p/>
 * <p/>
 * 样例见：PayTest
 * <p/>
 * code为SUCCESS才需要校验签名， FAIL则不需要校验签名。
 * <p/>
 * 计算签名，校验签名策略有两种：
 * 使用sdk，那么 依赖的model 一定要和后端的保持一致，否则签名错误。  推荐在服务端使用，BeanUtil.object2Map
 * 用json来校验则没有问题。  推荐在业务方使用，  BeanUtil.jsonStr2Map(ret)
 * <p/>
 * 采用fastjson，不采用Gson，是因为 gson转json的时候，如果等号，则转成\u003d， 而fastjson则直接转成'='
 */

@RestController
@RequestMapping("/pay")
public class PayController {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PayService payService;

    /**
     * 扫码支付，网页支付，app支付 统一下单接口， 此接口不支持刷卡支付
     * <p/>
     * 微信扫码支付，使用模式二。
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/request", method = RequestMethod.POST)
    @ResponseBody
    public String request(HttpServletRequest request) {

        String data = null;

        PayRequest req = null;
        try {
            data = IOUtils.toString(request.getInputStream());

            logger.info("data is " + data);
            req = JSON.parseObject(data, PayRequest.class);

            return payService.request(req).toJsonString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResponse.createErrorResponse(e.getMessage()).toJsonString();
        }
    }

    /***
     * 查询 订单
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Object query(HttpServletRequest request) {

        String data = null;

        PayQueryRequest req = null;
        try {
            data = IOUtils.toString(request.getInputStream());

            logger.info("data is " + data);
            req = JSON.parseObject(data, PayQueryRequest.class);

            return payService.query(req).toJsonString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResponse.createErrorResponse(e.getMessage()).toJsonString();
        }
    }

    /***
     * 同步订单状态，与第三方支付的订单状态保持一致
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/synchronize", method = RequestMethod.POST)
    @ResponseBody
    public Object synchronize(HttpServletRequest request) {

        String data = null;

        PayQueryRequest req = null;
        try {
            data = IOUtils.toString(request.getInputStream());

            logger.info("data is " + data);
            req = JSON.parseObject(data, PayQueryRequest.class);

            return payService.synchronize(req).toJsonString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResponse.createErrorResponse(e.getMessage()).toJsonString();
        }
    }
}
