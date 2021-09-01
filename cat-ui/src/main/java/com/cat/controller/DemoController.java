package com.cat.controller;

import cn.hutool.Hutool;
import cn.hutool.http.HttpUtil;
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

//    @Resource
//    private BusinessOrderService businessOrderService;

    @RequestMapping(value ="/start", produces="application/json;charset=UTF-8")
    public String start() throws InterruptedException {
        log.info("start ...");
        String response = HttpUtil.get("cat-business.default.svc.cluster.local:8083/business-order");
//        String response = businessOrderService.businessOrder();
//        Thread.sleep(100);
        log.info("end ... result : {}", response);
        return response;

    }

    @RequestMapping(value ="/start2", produces="application/json;charset=UTF-8")
    public String start2() throws InterruptedException {
        log.info("start2 ...");
        String response = restTemplate.getForObject("http://" + businessAddress + "/business-order", String.class);
//        String response = businessOrderService.businessOrder();
//        Thread.sleep(100);
        log.info("end ... result : {}", response);
        return response;

    }

//    public static void main(String[] args) {
//        double[] arrs = {2,4,5,1,3,3,4,1,2,6};
//        List<Double> doubleList = new ArrayList<>();
//        for (double currArr : arrs) {
//            doubleList.add(currArr);
//        }
//        double percentile = getPercentile(doubleList, 0.95);
//        System.out.println(percentile);
//    }
//
//    private static double getPercentile(List<Double> dataList, double p) {
//        int n = dataList.size();
//        dataList.sort(new Comparator<Double>() {
//            //从小到大排序
//            @Override
//            public int compare(Double o1, Double o2) {
//                if(o1 == null || o2== null){
//                    return 0;
//                }
//                return o1.compareTo(o2);
//            }
//        });
//        double px =  p*(n-1);
//        int i = (int)java.lang.Math.floor(px);
//        double g = px - i;
//        if(g==0){
//            return dataList.get(i);
//        }else{
//            return (1-g)*dataList.get(i)+g*dataList.get(i+1);
//        }
//    }

}
