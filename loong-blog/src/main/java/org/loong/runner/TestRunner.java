package org.loong.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 测试运行器组件，实现 CommandLineRunner 接口。
 * 当 Spring Boot 应用启动完成时，此组件会执行 run 方法。
 */
@Component
public class TestRunner implements CommandLineRunner {
    /**
     * 在 Spring Boot 应用启动完成后执行此方法。
     * @param args 命令行参数，启动时可以传递给应用的参数。
     * @throws Exception 如果运行时发生异常，可以抛出。
     */
    @Override
    public void run(String... args) throws Exception {
        // 打印程序初始化信息
        System.out.println("程序初始化");
    }
}
