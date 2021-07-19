package com.cat.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: DemoController
 * @Author: wutuo
 * @Date: 2021-07-15
 * @Version:v1.0
 */
@RestController
public class DemoController {

    private static final Logger log = LoggerFactory.getLogger(DemoController.class);

    @GetMapping("/storage/reduce")
    public String create() throws InterruptedException {

        log.info("start reduce storage ...");
        Thread.sleep(2000);
        log.info("end reduce storage ...");
        return "reduce storage success";
    }
}
