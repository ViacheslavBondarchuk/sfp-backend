package com.org.house.sfpbackend.utils;

import com.org.house.sfpbackend.model.sql.User;
import com.org.house.sfpbackend.repository.sql.UserRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public final class UserCleanerUtils {
    @Autowired
    private UserRepository userRepository;


    @Scheduled(cron = "0 0 * * * ?")
    public void deleteAlldisabledUserAccounts() throws NotFoundException {
        List<User> disabledAccounts = userRepository
                .findByEnabled(false).orElseThrow(() -> new NotFoundException("No disabled accounts"));
        if (disabledAccounts != null && disabledAccounts.size() > 0) {
            disabledAccounts.forEach(disabledAccount -> {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(disabledAccount.getRegDate());
                calendar.add(7, Calendar.DAY_OF_MONTH);
                boolean before = calendar.before(new Date());
                if (before) userRepository.delete(disabledAccount);
            });
        }
    }

}
