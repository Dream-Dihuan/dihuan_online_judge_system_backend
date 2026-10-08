package com.dihuan.ai.config;

import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
@MapperScan(
        basePackages = "com.dihuan.ai.persistence.mapper",
        sqlSessionFactoryRef = "chatMemorySqlSessionFactory")
public class AIChatMybatisConfig {

    @Bean
    @Primary
    public SqlSessionFactory chatMemorySqlSessionFactory(
            DataSource chatMemoryDataSource) throws Exception {
        MybatisSqlSessionFactoryBean factoryBean = new MybatisSqlSessionFactoryBean();
        factoryBean.setDataSource(chatMemoryDataSource);
        return factoryBean.getObject();
    }
}