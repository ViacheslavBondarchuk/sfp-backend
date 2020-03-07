package com.org.house.sfpbackend.service.impl;

import com.org.house.sfpbackend.model.Authority;
import com.org.house.sfpbackend.model.sql.User;
import com.org.house.sfpbackend.repository.sql.UserRepository;
import com.org.house.sfpbackend.service.AbstractService;
import com.org.house.sfpbackend.service.mail.MailService;
import com.org.house.sfpbackend.utils.AuthUtils;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Collections;
import java.util.Date;
import java.util.UUID;

@Service
public class UserService extends AbstractService<User, UserRepository> implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MailService mailService;

    @Value("${mail.activations.address}")
    private String activationAddress;


    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }

    @Transactional
    public User create(User user) throws MessagingException {
        User savedUser = super.create(prepareUser(user));
        sendActivationMail(user.getFullname(), user.getEmail(), savedUser.getId());
        return savedUser;
    }

    public void update(User user) throws NotFoundException {
        super.update(prepareUser(user), AuthUtils.getUserId());
    }

    public Iterable<User> readAll() {
        return super.readAll();
    }

    public User read() throws NotFoundException {
        return super.read(AuthUtils.getUserId());
    }

    public void delete(long id) {
        super.delete(AuthUtils.getUserId());
    }

    public void activate(long id) throws NotFoundException {
        repository.findById(id).map(dbUser -> {
            dbUser.setEnabled(true);
            return repository.save(dbUser);
        }).orElseThrow(() -> new NotFoundException("User has been not found"));
    }

    private void sendActivationMail(String fullname, String to, long id) throws MessagingException {
        String link = new StringBuilder()
                .append(activationAddress)
                .append(UUID.randomUUID().toString())
                .append("/")
                .append(id)
                .toString();
        mailService.send(fullname, link, to);
    }

    private User prepareUser(User user) {
        user.setRegDate(new Date());
        user.setEnabled(false);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        user.setCredentialsNonExpired(true);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setAuthorities(Collections.singleton(Authority.USER));
        return user;

    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        return repository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException("User by username: %s has been not found"));
    }
}
