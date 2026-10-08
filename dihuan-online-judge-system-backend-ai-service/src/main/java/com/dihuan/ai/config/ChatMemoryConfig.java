package com.dihuan.ai.config;

import com.zaxxer.hikari.HikariDataSource;
import com.dihuan.ai.service.ChatMemoryService;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;

@Configuration
public class ChatMemoryConfig {

    @Bean
    @ConfigurationProperties("spring.chat-memory.datasource")
    public DataSource chatMemoryDataSource() {
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }

    @Bean
    public ChatMemory chatMemory(ChatMemoryService chatMemoryService) {
        return new JdbcChatMemory(chatMemoryService);
    }
}