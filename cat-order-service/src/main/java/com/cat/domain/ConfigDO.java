package com.cat.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

/**
 * @Description: ConfigDO
 * @Author: wutuo
 * @Date: 2021-07-16
 * @Version:v1.0
 */
@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDO {
    private Integer id;

    private String name;

    private String content;

    private Date creationDate;

    private Date modifyDate;
}
