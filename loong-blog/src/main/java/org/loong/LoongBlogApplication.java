package org.loong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@MapperScan("org.loong.mapper")
public class LoongBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoongBlogApplication.class, args);
    }
}
