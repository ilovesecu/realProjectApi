package com.playground.real_project_api.config;

import com.playground.real_project_api.utils.annotation.RealProjectDb;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import javax.sql.DataSource;

@Configuration
@MapperScan(annotationClass = RealProjectDb.class, basePackages = "com.playground.real_project_api.proc", sqlSessionFactoryRef = "pgfurpSessionFactory")
public class DatasourceConfig {
    @Value("${mybatis.mapper-locations}") String mapperLocation = "";

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties(){return new DataSourceProperties();}

    @Bean(name = "pgfurp", destroyMethod = "close")
    public HikariDataSource rpDatasource(DataSourceProperties properties){
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class).build();
    }

    @Bean(name = "pgfurpSessionFactory")
    public SqlSessionFactory rpSqlSessionFactory(@Qualifier("pgfurp") DataSource dataSource)throws Exception{
        return new DatabaseSqlSessionFactory().getSqlFactory(dataSource, mapperLocation);
    }
}
