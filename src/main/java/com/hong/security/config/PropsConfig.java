package com.hong.security.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wanghong
 * @date 2020/05/11 22:09
 **/
@Setter
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "myprops")
public class PropsConfig {

    public boolean checkSignOpen;

    public boolean checkTokenOpen;

    public boolean isDevEnv;

    public List<String> allowOriginUrls;

    public List<String> skipSignUrls;

    public List<String> skipTokenUrls;
}
