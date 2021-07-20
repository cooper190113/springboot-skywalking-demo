package com.cat.config;

import feign.Feign;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.commons.httpclient.OkHttpClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.support.FeignHttpClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * @Description: FeignOkHttpConfig
 * @Author: wutuo
 * @Date: 2021-07-20
 * @Version:v1.0
 */
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignOkHttpConfig {

    private OkHttpClient okHttpClient;

    //注入okhttp
    @Bean
    public OkHttpClient okHttpClient(OkHttpClientFactory okHttpClientFactory,
                                             FeignHttpClientProperties httpClientProperties) {
        this.okHttpClient = okHttpClientFactory.createBuilder(httpClientProperties.isDisableSslValidation())
                .connectTimeout(httpClientProperties.getConnectionTimeout(),TimeUnit.SECONDS)
                .followRedirects(httpClientProperties.isFollowRedirects())
                .build();
        return this.okHttpClient;
    }
}
