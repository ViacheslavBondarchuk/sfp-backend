package com.org.house.sfpbackend.service.impl;

import com.org.house.sfpbackend.model.Authority;
import com.org.house.sfpbackend.model.sql.User;
import com.org.house.sfpbackend.repository.sql.UserRepository;
import com.org.house.sfpbackend.service.AbstractService;
import com.org.house.sfpbackend.utils.AuthUtils;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserService extends AbstractService<User, UserRepository> implements UserDetailsService {
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public UserService(UserRepository repository) {
        super(repository);
    }

    public void create(User user) {
        super.create(prepareUser(user));
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

    private User prepareUser(User user) {
        user.setEnabled(true);
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
