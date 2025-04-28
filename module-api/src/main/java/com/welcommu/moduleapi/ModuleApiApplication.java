package com.welcommu.moduleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = { "com.welcommu.moduleapi", "com.welcommu.modulecommon", "com.welcommu.moduleinfra", "com.welcommu.moduleservice"})
@EntityScan("com.welcommu.moduledomain")
@EnableJpaRepositories("com.welcommu.moduleinfra")
public class ModuleApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(ModuleApiApplication.class, args);
    }

}
