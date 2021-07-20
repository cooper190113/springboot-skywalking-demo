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

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @Resource
    private RestTemplate restTemplate;

    @Value("${service.order.address:localhost:8084}")
    private String orderAddress;
    @Value("${service.storage.address:localhost:8085}")
    private String storageAddress;

    @GetMapping("/business-order")
    public String businessOrder() throws InterruptedException {
        Thread.sleep(2000);
        log.info("business start...");
        String response1 = restTemplate.getForObject("http://" + orderAddress + "/order/create", String.class);
        String response2 = restTemplate.getForObject("http://" + storageAddress + "/storage/reduce", String.class);
        return String.format("business end...", response1, response2);
    }
}
