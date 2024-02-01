package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;


import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        Optional<User> userFromDb = Optional.ofNullable(userRepository.findByUsername(username));
        return userFromDb.orElseThrow(() -> new EntityNotFoundException("Пользователь с именем " + username + " не найден"));
    }


    @Transactional(readOnly = true)
    @Override
    public User getById(Long id) {
        Optional<User> userFromDb = userRepository.findById(id);
        return userFromDb.orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        User userFromDb = userRepository.getById(user.getId());
        if (userFromDb == null) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        userFromDb.setName(user.getName());
        userFromDb.setLastname(user.getLastname());
        userFromDb.setAge(user.getAge());
        userFromDb.setEmail(user.getEmail());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            userFromDb.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userFromDb.setRoles(user.getRoles());
        return userRepository.save(userFromDb);
    }

    @Override
    public void save(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb != null) {
            throw new DuplicateKeyException("Такой пользователь уже существует");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void delete(User user) {
        User userFromDb = userRepository.findByUsername(user.getUsername());
        if (userFromDb == null) {
            throw new EntityNotFoundException("Пользователь не найден");
        }
        userRepository.delete(userFromDb);
    }

}
