package com.cat.config;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.springframework.util.ReflectionUtils;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 对MyBatis进行拦截，添加Cat监控
 *
 * @author wutuo
 */
@Intercepts({
        @Signature(method = "query", type = Executor.class, args = {
                MappedStatement.class, Object.class, RowBounds.class,
                ResultHandler.class}),
        @Signature(method = "update", type = Executor.class, args = {MappedStatement.class, Object.class})
})
public class CatMybatisPlugin implements Interceptor {

    private static final Pattern PARAMETER_PATTERN = Pattern.compile("\\?");
    private static final String MYSQL_DEFAULT_URL = "jdbc:mysql://UUUUUKnown:3306/%s?useUnicode=true";
    private Executor target;

    // druid 数据源的类名称
    private static final String DruidDataSourceClassName = "com.alibaba.druid.pool.DruidDataSource";
    // dbcp 数据源的类名称
    private static final String DBCPBasicDataSourceClassName = "org.apache.commons.dbcp.BasicDataSource";
    // dbcp2 数据源的类名称
    private static final String DBCP2BasicDataSourceClassName = "org.apache.commons.dbcp2.BasicDataSource";
    // c3p0 数据源的类名称
    private static final String C3P0ComboPooledDataSourceClassName = "com.mchange.v2.c3p0.ComboPooledDataSource";
    // HikariCP 数据源的类名称
    private static final String HikariCPDataSourceClassName = "com.zaxxer.hikari.HikariDataSource";
    // BoneCP 数据源的类名称
    private static final String BoneCPDataSourceClassName = "com.jolbox.bonecp.BoneCPDataSource";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = this.getStatement(invocation);
        String methodName = this.getMethodName(mappedStatement);
        Transaction t = Cat.newTransaction("SQL", methodName);

        String sql = this.getSql(invocation, mappedStatement);
        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Cat.logEvent("SQL.Method", sqlCommandType.name().toLowerCase(), Message.SUCCESS, sql);

        String url = this.getSQLDatabaseUrlByStatement(mappedStatement);
        Cat.logEvent("SQL.Database", url);

        return doFinish(invocation, t);
    }

    private MappedStatement getStatement(Invocation invocation) {
        return (MappedStatement) invocation.getArgs()[0];
    }

    private String getMethodName(MappedStatement mappedStatement) {
        String[] strArr = mappedStatement.getId().split("\\.");
        String methodName = strArr[strArr.length - 2] + "." + strArr[strArr.length - 1];

        return methodName;
    }

    private String getSql(Invocation invocation, MappedStatement mappedStatement) {
        Object parameter = null;
        if (invocation.getArgs().length > 1) {
            parameter = invocation.getArgs()[1];
        }

        BoundSql boundSql = mappedStatement.getBoundSql(parameter);
        Configuration configuration = mappedStatement.getConfiguration();
        String sql = sqlResolve(configuration, boundSql);

        return sql;
    }

    private Object doFinish(Invocation invocation, Transaction t) throws InvocationTargetException, IllegalAccessException {
        Object returnObj = null;
        try {
            returnObj = invocation.proceed();
            t.setStatus(Transaction.SUCCESS);
        } catch (Exception e) {
            Cat.logError(e);
            throw e;
        } finally {
            t.complete();
        }

        return returnObj;
    }


    private String getSQLDatabaseUrlByStatement(MappedStatement mappedStatement) {
        String url = null;
        DataSource dataSource = null;
        try {
            Configuration configuration = mappedStatement.getConfiguration();
            Environment environment = configuration.getEnvironment();
            dataSource = environment.getDataSource();

            url = switchDataSource(dataSource);

            return url;
        } catch (NoSuchFieldException | IllegalAccessException | NullPointerException e) {
            Cat.logError(e);
        }

        Cat.logError(new Exception("UnSupport type of DataSource : " + dataSource.getClass().toString()));
        return MYSQL_DEFAULT_URL;
    }

    private String switchDataSource(DataSource dataSource) throws NoSuchFieldException, IllegalAccessException {
        String url = null;
        if (dataSource != null) {
            // 处理常见的数据源
            switch (dataSource.getClass().getName()) {
                // druid
                case DruidDataSourceClassName:
                    url = getDataSourceSqlURL(dataSource, DruidDataSourceClassName, "getUrl");
                    break;
                // dbcp
                case DBCPBasicDataSourceClassName:
                    url = getDataSourceSqlURL(dataSource, DBCPBasicDataSourceClassName, "getUrl");
                    break;
                // dbcp2
                case DBCP2BasicDataSourceClassName:
                    url = getDataSourceSqlURL(dataSource, DBCP2BasicDataSourceClassName, "getUrl");
                    break;
                // c3p0
                case C3P0ComboPooledDataSourceClassName:
                    url = getDataSourceSqlURL(dataSource, C3P0ComboPooledDataSourceClassName, "getJdbcUrl");
                    break;
                // HikariCP
                case HikariCPDataSourceClassName:
                    url = getDataSourceSqlURL(dataSource, HikariCPDataSourceClassName, "getJdbcUrl");
                    break;
                // BoneCP
                case BoneCPDataSourceClassName:
                    url = getDataSourceSqlURL(dataSource, BoneCPDataSourceClassName, "getJdbcUrl");
                    break;
            }
        }
        return url;
    }

    /**
     * 获取数据源的SQL地址
     *
     * @param dataSource                 数据源
     * @param runtimeDataSourceClassName 运行时真实的数据源的类名称
     * @param sqlURLMethodName           获取SQL地址的方法名称
     */
    private String getDataSourceSqlURL(DataSource dataSource, String runtimeDataSourceClassName, String sqlURLMethodName) {
        Class<?> dataSourceClass = null;
        try {
            dataSourceClass = Class.forName(runtimeDataSourceClassName);
        } catch (ClassNotFoundException e) {
        }
        Method sqlURLMethod = ReflectionUtils.findMethod(dataSourceClass, sqlURLMethodName);
        return (String) ReflectionUtils.invokeMethod(sqlURLMethod, dataSource);
    }

    public String sqlResolve(Configuration configuration, BoundSql boundSql) {
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql = boundSql.getSql().replaceAll("[\\s]+", " ");
        if (parameterMappings.size() > 0 && parameterObject != null) {
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", Matcher.quoteReplacement(resolveParameterValue(parameterObject)));

            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                Matcher matcher = PARAMETER_PATTERN.matcher(sql);
                StringBuffer sqlBuffer = new StringBuffer();
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    Object obj = null;
                    if (metaObject.hasGetter(propertyName)) {
                        obj = metaObject.getValue(propertyName);
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        obj = boundSql.getAdditionalParameter(propertyName);
                    }
                    if (matcher.find()) {
                        matcher.appendReplacement(sqlBuffer, Matcher.quoteReplacement(resolveParameterValue(obj)));
                    }
                }
                matcher.appendTail(sqlBuffer);
                sql = sqlBuffer.toString();
            }
        }
        return sql;
    }

    private String resolveParameterValue(Object obj) {
        String value = null;
        if (obj instanceof String) {
            value = "'" + obj.toString() + "'";
        } else if (obj instanceof Date) {
            DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.CHINA);
            value = "'" + formatter.format((Date) obj) + "'";
        } else {
            if (obj != null) {
                value = obj.toString();
            } else {
                value = "";
            }

        }
        return value;
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            this.target = (Executor) target;
            return Plugin.wrap(target, this);
        }
        return target;
    }

    @Override
    public void setProperties(Properties properties) {
    }

}