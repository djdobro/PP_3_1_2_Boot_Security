package ru.kata.spring.boot_security.demo.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserServiceImpl userServiceImpl;

    private final RoleRepository roleRepository;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, RoleRepository roleRepository) {
        this.userServiceImpl = userServiceImpl;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public String listOfUsers(Model model) {
        model.addAttribute("allUsers", userServiceImpl.allUsers());
        return "admin/allUsers";
    }

    @GetMapping("/new")
    public String newUser(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/new";
    }

    @PostMapping("/new")
    public String addNewUserToDb(@ModelAttribute("user") @Valid User user) {
        userServiceImpl.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/edit/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userServiceImpl.getById(id));
        model.addAttribute("roles", roleRepository.findAll());
        return "admin/edit";
    }

    @PostMapping("/edit/{id}")
    public String editUserFromDb(@ModelAttribute("user") @Valid User user) {
        userServiceImpl.update(user);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        User user = userServiceImpl.getById(id);
        user.getRoles().clear();
        userServiceImpl.delete(userServiceImpl.getById(id));
        return "redirect:/admin";
    }

}
