package com.xmcc.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "wechat")//配置文件的前缀为wechat
@Data
public class WeiXinProperties {

    private String appid;
    private String secret;
    //商户号
    private String mcgId;
    //商户密钥
    private String mchKey;
    //商户证书路径
    private String keyPath;
    //商户支付异步通知
    private String notifyUrl;
}
