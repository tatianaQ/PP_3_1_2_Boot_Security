package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void create(User user) {
        // Кодирование пароля перед сохранением
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Установка роли пользователя
        Set<Role> roles = new HashSet<>();
        roles.add(roleRepository.findByRole("ROLE_USER"));
        user.setRoles(roles);

        // Сохранение пользователя
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void update(long id, User user) {
        // Получение существующего пользователя по ID
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        existingUser.setEmail(user.getEmail());
        existingUser.setFirstName(user.getFirstName());
        existingUser.setLastName(user.getLastName());
        existingUser.setAge(user.getAge());
        existingUser.setRoles(user.getRoles());

        // Если пароль был изменен, кодируем его и обновляем
        String newPassword = user.getPassword();
        if (newPassword != null && !newPassword.equals(existingUser.getPassword())) {
            existingUser.setPassword(passwordEncoder.encode(newPassword));
        }

        // Сохранение обновленных данных пользователя
        userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void delete(long id) {
        // Удаление пользователя по ID
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public User findById(long id) {
        // Поиск пользователя по ID
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public List<User> getAll() {
        // Получение списка всех пользователей
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public List<Role> getAllRoles() {
        // Получение списка всех ролей
        return roleRepository.findAll();
    }
    @Override
    @Transactional
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User not found with email: " + email));
    }
}
