package ru.kata.spring.boot_security.demo.service;

import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import javax.transaction.Transactional;
import java.util.List;

@Service
public interface UserService {

    User findById(long id);
    List<User> getAll();
    List<Role> getAllRoles();
    void update(long id, User user);
    void create(User user);
    void delete(long id);

    @Transactional
    User findByEmail(String email);
}