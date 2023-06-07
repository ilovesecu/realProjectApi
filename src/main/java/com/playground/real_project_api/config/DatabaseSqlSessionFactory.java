package com.playground.real_project_api.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

public class DatabaseSqlSessionFactory {
    public SqlSessionFactory getSqlFactory(DataSource dataSource, String mapperLocation)throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        Resource[] mappers = new PathMatchingResourcePatternResolver().getResources(mapperLocation);
        sqlSessionFactoryBean.setMapperLocations(mappers);
        sqlSessionFactoryBean.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sqlSessionFactoryBean.getObject();
    }
}
