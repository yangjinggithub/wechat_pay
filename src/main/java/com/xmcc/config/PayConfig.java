package com.xmcc.config;

import com.lly835.bestpay.config.WxPayH5Config;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.xmcc.properties.WeiXinProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayConfig {
    @Autowired
    private WeiXinProperties weiXinProperties;

    @Bean
    public BestPayService bestPayService(){
        WxPayH5Config wxPayH5Config = new WxPayH5Config();
        //设置公众号appid
        wxPayH5Config.setAppId(weiXinProperties.getAppid());
        //设置公众号密钥
        wxPayH5Config.setAppSecret(weiXinProperties.getSecret());
        //设置商户号
        wxPayH5Config.setMchId(weiXinProperties.getMcgId());
        //设置商户密钥
        wxPayH5Config.setMchKey(weiXinProperties.getMchKey());
        //设置商户证书路径
        wxPayH5Config.setKeyPath(weiXinProperties.getKeyPath());
        //设置异步通知路径
        wxPayH5Config.setNotifyUrl(weiXinProperties.getNotifyUrl());

        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayH5Config(wxPayH5Config);
        return bestPayService;
    }
}
