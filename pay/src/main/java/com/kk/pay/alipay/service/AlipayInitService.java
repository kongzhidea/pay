package com.kk.pay.alipay.service;

import com.alipay.demo.trade.config.Configs;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class AlipayInitService {

    @PostConstruct
    private void init() {
        /** 一定要在创建AlipayTradeService之前调用Configs.init()设置默认参数
         *  Configs会读取classpath下的alipayrisk10.properties文件配置信息，如果找不到该文件则确认该文件是否在classpath目录
         */
        Configs.init("zfbinfo.properties");
    }
}
