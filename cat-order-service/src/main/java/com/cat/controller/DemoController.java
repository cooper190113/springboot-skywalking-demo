package com.cat.controller;

import com.cat.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private ConfigService configService;

    @GetMapping("/order/create")
    public String create() throws InterruptedException {
        log.info("start order ...");
        log.info(configService.getConfig().toString());
        Thread.sleep(2000);
        log.info("end order ...");
        return "order success";
    }
}
