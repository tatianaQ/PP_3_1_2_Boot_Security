package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Details;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import javax.validation.Valid;
import java.util.List;

@Controller
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin")
    public String getAll(Model model) {
        List<User> users = userService.getAll();
        model.addAttribute("allUsers", users);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Details user = (Details) authentication.getPrincipal();

        model.addAttribute("user", user);

        return "admin";
    }

    @GetMapping("/admin/update")
    public String updateNewForm(Model model, @RequestParam("id") long id) {
        User user = userService.findById(id);
        List<Role> allRoles = userService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("allRoles", allRoles);
        return "update";
    }

    @PatchMapping("/admin/update")
    public String update(@RequestParam("id") long id, @ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "update";
        }
        userService.update(id, user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/new")
    public String addUser(Model model) {
        List<Role> roles = userService.getAllRoles();
        model.addAttribute("new_user", new User());
        model.addAttribute("allRoles", roles);
        return "addUser";
    }

    @PostMapping("/admin/new")
    public String createUser(@ModelAttribute("new_user") @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "addUser";
        }
        userService.create(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete")
    public String delete(@RequestParam("id") long id) {
        userService.delete(id);
        return "redirect:/admin";
    }
}
