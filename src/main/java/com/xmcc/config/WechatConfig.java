package com.xmcc.config;

import com.xmcc.properties.WeiXinProperties;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WechatConfig {

    @Autowired
    private WeiXinProperties weiXinProperties;
    @Bean
    public WxMpService wxMpService(){
        WxMpServiceImpl wxMpService = new WxMpServiceImpl();
        wxMpService.setWxMpConfigStorage(wxMpConfigStorage());
        return wxMpService;
    }
    @Bean
    public WxMpConfigStorage wxMpConfigStorage(){
        WxMpInMemoryConfigStorage wxMpInMemoryConfigStorage = new WxMpInMemoryConfigStorage();
        //设置appid
        wxMpInMemoryConfigStorage.setAppId(weiXinProperties.getAppid());
        //设置密码
        wxMpInMemoryConfigStorage.setSecret(weiXinProperties.getSecret());
        return wxMpInMemoryConfigStorage;
    }
}
