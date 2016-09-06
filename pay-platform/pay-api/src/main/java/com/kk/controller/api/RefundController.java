package com.kk.controller.api;

import com.alibaba.fastjson.JSON;
import com.kk.api.ApiResponse;
import com.kk.api.request.RefundQueryRequest;
import com.kk.api.request.RefundRequest;
import com.kk.api.response.RefundResponse;
import com.kk.api.service.RefundService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 订单退款
 */
@RestController
@RequestMapping("/refund")
public class RefundController {
    private Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    private RefundService refundService;

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    @ResponseBody
    public Object request(HttpServletRequest request) {
        String data = null;

        RefundRequest req = null;
        try {
            data = IOUtils.toString(request.getInputStream());

            logger.info("data is " + data);
            req = JSON.parseObject(data, RefundRequest.class);

            return refundService.request(req).toJsonString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResponse.createErrorResponse(e.getMessage()).toJsonString();
        }
    }

    /**
     * 退款查询
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public Object query(HttpServletRequest request) {
        String data = null;

        RefundQueryRequest req = null;
        try {
            data = IOUtils.toString(request.getInputStream());

            logger.info("data is " + data);
            req = JSON.parseObject(data, RefundQueryRequest.class);

            return refundService.query(req).toJsonString();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ApiResponse.createErrorResponse(e.getMessage()).toJsonString();
        }
    }
}
