package com.cat.service.impl;

import com.cat.domain.ConfigDO;
import com.cat.mapper.ConfigMapper;
import com.cat.service.ConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Description: ConfigServiceImpl
 * @Author: wutuo
 * @Date: 2021-07-16
 * @Version:v1.0
 */
@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigMapper configMapper;

    @Override
    public List<ConfigDO> getConfig() {
        return configMapper.getConfig();
    }
}
