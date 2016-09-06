package com.kk.controller.notify;

import com.kk.api.service.PayService;
import com.kk.platform.enums.PayTypeCode;
import com.kk.wechat.response.ResultCode;
import com.kk.wechat.util.WeixinUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 微信支付（扫码，wap，app支付均会回调）成功后 回调接口
 */
@RestController
@RequestMapping("/notify/weixin")
public class WechatPayNotifyController {
    private Log logger = LogFactory.getLog(this.getClass());

    @Autowired
    private PayService payService;

    @RequestMapping(value = "", method = {RequestMethod.GET, RequestMethod.POST})
    public Object receiveNotify(HttpServletRequest request) {
        try {
            String xml = IOUtils.toString(request.getInputStream());
            logger.info("weixin pay notify result is：" + xml);
            if (StringUtils.isBlank(xml)) {
                return WeixinUtil.getResult(ResultCode.FAIL, "接受参数为空");
            }

            if (payService.handlePayNotify(PayTypeCode.WECHAT_PAY, xml)) {
                return WeixinUtil.getResult(ResultCode.SUCCESS, "OK");
            } else {
                return WeixinUtil.getResult(ResultCode.FAIL, "FAIL");
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return WeixinUtil.getResult(ResultCode.FAIL, "PARSE MESSAGE ERROR");
        }

    }
}
