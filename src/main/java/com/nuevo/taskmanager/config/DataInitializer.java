package com.nuevo.taskmanager.config;

import com.nuevo.taskmanager.entity.TaskStatus;
import com.nuevo.taskmanager.entity.User;
import com.nuevo.taskmanager.repository.TaskStatusRepository;
import com.nuevo.taskmanager.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner loadData(TaskStatusRepository statusRepo,
                                      UserRepository userRepo,
                                      BCryptPasswordEncoder encoder) {
        return args -> {
            if (statusRepo.count() == 0) {
                statusRepo.save(new TaskStatus(null, "PENDING"));
                statusRepo.save(new TaskStatus(null, "IN_PROGRESS"));
                statusRepo.save(new TaskStatus(null, "DONE"));
            }

            if (userRepo.count() == 0) {
                userRepo.save(new User(null, "admin", encoder.encode("admin123")));
                userRepo.save(new User(null, "user", encoder.encode("user123")));
            }
        };
    }
}
