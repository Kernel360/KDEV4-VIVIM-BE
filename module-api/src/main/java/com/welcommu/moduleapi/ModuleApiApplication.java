package com.welcommu.moduleapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(
        scanBasePackages = { "com.welcommu.moduleapi", "com.welcommu.modulecommon", "com.welcommu.modulerepository", "com.welcommu.moduleservice"}
)
@EntityScan("com.welcommu.moduledomain")
@EnableJpaRepositories("com.welcommu.modulerepository")
@ComponentScan("com.welcommu")
public class ModuleApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleApiApplication.class, args);
    }

}
