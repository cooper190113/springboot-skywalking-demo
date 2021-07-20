package com.cat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description: UIApplication
 * @Author: wutuo
 * @Date: 2021-07-15
 * @Version:v1.0
 * https://www.cnblogs.com/chinaWu/p/13575813.html
 * https://www.freesion.com/article/3159467684/
 */
@EnableFeignClients
@EnableDiscoveryClient
@SpringBootApplication
public class CatUiApplication {
    public static void main(String[] args) {
        SpringApplication.run( CatUiApplication.class, args );
    }
}
