package com.cat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * https://www.cnblogs.com/jimoliunian/p/14676009.html
 */
@Configuration
public class LoggingAutoConfiguration {
    @Bean
    public OkHttpLoggingInterceptor okHttpLoggingInterceptor() {
        return new OkHttpLoggingInterceptor();
    }
}
