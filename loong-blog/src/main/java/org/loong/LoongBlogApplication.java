package org.loong;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("org.loong.mapper")
@EnableScheduling
@EnableSwagger2
public class LoongBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LoongBlogApplication.class, args);
    }
}
