package com.dihuan.common.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
//扫描 mapper，不用一个一个地去加@Mapper的注解
@MapperScan("com.dihuan.*.mapper")
public class MybatisPlusConfiguration {
}
