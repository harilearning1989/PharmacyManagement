package com.web.pharma.auth.scheduler;

import com.web.pharma.auth.entities.User;
import com.web.pharma.auth.repos.UserRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class PasswordExpiryScheduler {

    private final UserRepository userRepository;

    public PasswordExpiryScheduler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Run every night at 2 AM
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkAndExpirePasswords() {
        List<User> users = userRepository.findAll();
        LocalDateTime now = LocalDateTime.now();

        for (User user : users) {
            if (user.getPasswordChangedAt() != null) {
                long days = ChronoUnit.DAYS.between(user.getPasswordChangedAt(), now);
                if (days > 90 && !user.isPasswordExpired()) {
                    user.setPasswordExpired(true);
                    userRepository.save(user);
                }
            }
        }
    }
}

