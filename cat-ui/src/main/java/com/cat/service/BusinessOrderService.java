package com.cat.service;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name = "cat-business-consumer")
public interface BusinessOrderService {
    @RequestMapping(value = "/business-order", method = RequestMethod.GET)
    String businessOrder();
}
