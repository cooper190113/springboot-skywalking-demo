package com.cat.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @Description: MyBatisConfig
 * @Author: wutuo
 * @Date: 2021-07-16
 * @Version:v1.0
 * mybatis
 * https://www.cnblogs.com/d0usr/p/12448639.html
 * https://www.cnblogs.com/xsyz/p/13820358.html
 * https://www.pianshen.com/article/5344308871/
 *
 * mybatis-plus
 * https://www.cnblogs.com/yinjihuan/p/13065070.html
 * https://www.cnblogs.com/yinjihuan/p/12712195.html
 * https://github.com/yinjihuan/kitty
 */
//@Configuration
////@AutoConfigureAfter(PageHelperAutoConfiguration.class)
//public class MyBatisConfig {
//    @Autowired
//    private List<SqlSessionFactory> sqlSessionFactoryList;
//
//    @PostConstruct
//    public void addMySqlInterceptor() {
//        CatMybatisPlugin interceptor = new CatMybatisPlugin();
//        for (SqlSessionFactory sqlSessionFactory : sqlSessionFactoryList) {
//            sqlSessionFactory.getConfiguration().addInterceptor(interceptor);
//        }
//    }
//}
