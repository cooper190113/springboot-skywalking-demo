package com.cat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

/**
 * @Description: DemoController
 * @Author: wutuo
 * @Date: 2021-07-15
 * @Version:v1.0
 */
@RestController
public class DemoController {

    @Resource
    private RestTemplate restTemplate;

    @Value("${service.business.address:localhost:8083}")
    private String businessAddress;

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/start")
    public String start() throws InterruptedException {
        log.info("start ...");
        String response = restTemplate.getForObject("http://" + businessAddress + "/business-order", String.class);
        Thread.sleep(100);
        log.info("end ... result : {}", response);
        return response;

    }
}
