package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    User findByUsername(String username);
    User getById(Long id);
    List<User> allUsers();
    User update(User user);
    void save(User user);
    void delete(User user);
}
