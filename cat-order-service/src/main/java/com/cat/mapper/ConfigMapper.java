package com.cat.mapper;

import com.cat.domain.ConfigDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description: UserMapper
 * @Author: wutuo
 * @Date: 2021-07-16
 * @Version:v1.0
 */
@Mapper
public interface ConfigMapper {

    List<ConfigDO> getConfig();
}
