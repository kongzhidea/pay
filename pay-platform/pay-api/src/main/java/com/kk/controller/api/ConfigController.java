package com.kk.controller.api;

import com.alibaba.fastjson.JSON;
import com.kk.api.ApiResponse;
import com.kk.api.request.PayTypeRequest;
import com.kk.api.service.ConfigService;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 调用实例见：ConfigTest
 */
@RestController
@RequestMapping("/config")
public class ConfigController {
    private Log logger = LogFactory.getLog(this.getClass());
    @Autowired
    ConfigService configService;

    @RequestMapping(value = "/payTypes", method = RequestMethod.POST)
    public String payTypes(HttpServletRequest request) {
        String data = null;
        try {
            data = IOUtils.toString(request.getInputStream());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return ApiResponse.createErrorResponse("参数解析失败").toJsonString();
        }
        logger.info("data is " + data);
        PayTypeRequest req = JSON.parseObject(data, PayTypeRequest.class);
        return configService.getPayTypes(req).toJsonString();
    }
}
