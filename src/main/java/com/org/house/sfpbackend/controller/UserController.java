package com.org.house.sfpbackend.controller;

import com.org.house.sfpbackend.model.sql.User;
import com.org.house.sfpbackend.service.impl.UserService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;

@RestController
@RequestMapping("/sfp/api/users")
public class UserController {
    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public void create(@RequestBody User user) throws MessagingException {
        userService.create(user);
    }

    @PutMapping
    public void update(@RequestBody User user) throws NotFoundException {
        userService.update(user);
    }

    @GetMapping
    public Iterable<User> readAll() {
        return userService.readAll();
    }

    @GetMapping("/activate/{uuid}/{id}")
    public void activation(@PathVariable long id) throws NotFoundException {
        userService.activate(id);
    }

    @GetMapping("/{id}")
    public User read(@PathVariable long id) throws NotFoundException {
        return userService.read();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        userService.delete(id);
    }
}
