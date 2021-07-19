//package com.cat.config;
//
//import com.zaxxer.hikari.HikariDataSource;
//import org.apache.ibatis.plugin.Interceptor;
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.mybatis.spring.SqlSessionFactoryBean;
//import org.mybatis.spring.SqlSessionTemplate;
//import org.mybatis.spring.annotation.MapperScan;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.boot.jdbc.DataSourceBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Primary;
//import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
//
//import javax.sql.DataSource;
//
///**
// * @Description: DataSourceConfig
// * @Author: wutuo
// * @Date: 2021-07-16
// * @Version:v1.0
// */
//@Configuration
//@MapperScan(basePackages = {"com.cat.mapper"}, sqlSessionTemplateRef = "sqlSessionTemplate")
//public class DataSourceConfig {
//
//    /**
//     * xml配置路径
//     */
//    @Value("${mybatis.mapper-locations}")
//    private String mapperLocations;
//
//    /**
//     * model路径
//     */
//    @Value("${mybatis.type-aliases-package}")
//    private String typeAliasesPackage;
//
//    /**
//     * 获取admin数据源
//     *
//     * @return DataSource
//     */
//    @Bean(name = "dataSource")
//    @ConfigurationProperties(prefix = "spring.datasource")
//    @Primary
//    public DataSource getDataSource() {
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//    }
//
//    /**
//     * 获取admin数据源的sqlSessionFactory
//     *
//     * @param dataSource
//     * @return
//     * @throws Exception
//     */
//    @Bean(name = "sqlSessionFactory")
//    @Primary
//    public SqlSessionFactory getSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
//        //创建SqlSessionFactoryBean
//        /*SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();*/
//        //注意：如果需要兼容mybatis-plus需要使用MybatisSqlSessionFactoryBean 代替 SqlSessionFactoryBean
//        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
//        //设置DataSource
//        sqlSessionFactory.setDataSource(dataSource);
//        //添加xml路径
//        sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
//        //添加model路径
//        sqlSessionFactory.setTypeAliasesPackage(typeAliasesPackage);
//
//        CatMybatisPlugin catMybatisPlugin = new CatMybatisPlugin();
//        sqlSessionFactory.setPlugins(new Interceptor[]{catMybatisPlugin});
//        return sqlSessionFactory.getObject();
//    }
//
//
//    /**
//     * admin数据源事务管理器
//     *
//     * @param dataSource
//     * @return
//     */
//    @Bean(name = "transactionManager")
//    @Primary
//    public DataSourceTransactionManager getDataSourceTransactionManager(@Qualifier("dataSource") DataSource dataSource) {
//        return new DataSourceTransactionManager(dataSource);
//    }
//
//    /**
//     * admin 数据源 sqlSessionTemplate
//     *
//     * @param sqlSessionFactory
//     * @return
//     */
//    @Bean(name = "sqlSessionTemplate")
//    @Primary
//    public SqlSessionTemplate getSqlSessionTemplate(@Qualifier("sqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
//        return new SqlSessionTemplate(sqlSessionFactory);
//    }
//}
