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
}
