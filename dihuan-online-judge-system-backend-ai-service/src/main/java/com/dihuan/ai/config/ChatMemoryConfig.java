package com.dihuan.ai.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
public class ChatMemoryConfig {

    @Bean
    @ConfigurationProperties("spring.chat-memory.datasource")
    public DataSource chatMemoryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public JdbcTemplate chatMemoryJdbcTemplate(
            @Qualifier("chatMemoryDataSource") DataSource chatMemoryDataSource) {
        return new JdbcTemplate(chatMemoryDataSource);
    }

    @Bean
    public ChatMemory chatMemory(
            @Qualifier("chatMemoryJdbcTemplate") JdbcTemplate chatMemoryJdbcTemplate) {
        return new JdbcChatMemory(chatMemoryJdbcTemplate);
    }
}