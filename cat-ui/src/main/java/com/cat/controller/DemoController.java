package com.cat.controller;

import com.cat.service.BusinessOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @Value("${service.business.address:localhost:8083}")
    private String businessAddress;

    @Resource
    private BusinessOrderService businessOrderService;

    @RequestMapping(value ="/start", produces="application/json;charset=UTF-8")
    public String start() throws InterruptedException {
        log.info("start ...");
//        String response = restTemplate.getForObject("http://" + businessAddress + "/business-order", String.class);
        String response = businessOrderService.businessOrder();
        Thread.sleep(100);
        log.info("end ... result : {}", response);
        return response;

    }
}
