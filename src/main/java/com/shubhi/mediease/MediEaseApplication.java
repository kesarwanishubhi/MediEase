package com.shubhi.mediease;

import com.shubhi.mediease.entity.Role;
import com.shubhi.mediease.entity.Users;
import com.shubhi.mediease.repo.UsersRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class MediEaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediEaseApplication.class, args);
    }

//    @Bean
    public CommandLineRunner commandLineRunner(UsersRepo usersRepo) {
        return args -> {
            usersRepo.save(Users.builder()
                    .accountLocked(false)
                    .phone("9711877902")
                    .email("admin@mediease.com")
                    .password("admin")
                    .username("admin")
                    .role(Role.ADMIN)
                    .build());
            };
    }
}
