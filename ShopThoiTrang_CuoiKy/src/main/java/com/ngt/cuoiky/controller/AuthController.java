package com.ngt.cuoiky.controller;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.service.RoleService;
import com.ngt.cuoiky.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;
    
    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupForm(Model model) {
        User user = new User();

        model.addAttribute("user", user);

        return "auth/signup";
    }
    @PostMapping("/signup")
    public String saveUser(User user, BindingResult errors, RedirectAttributes redirectAttributes, HttpServletRequest request)
            throws UnsupportedEncodingException {
        if (user.getLastName().matches(".*\\d+.*")) {
            errors.rejectValue("lastName", "user", "Họ không được chứa số!");
        }
        if (user.getLastName().matches(".*[:;/{}*<>=()!.#$@_+,?-]+.*")) {
            errors.rejectValue("lastName", "user", "Họ không được chứa ký tự đặc biệt!");
        }
        if (user.getFirstName().matches(".*\\d+.*")) {
            errors.rejectValue("firstName", "user", "Tên không được chứa số!");
        }
        if (user.getFirstName().matches(".*[:;/{}*<>=()!.#$@_+,?-]+.*")) {
            errors.rejectValue("firstName", "user", "Tên không được chứa ký tự đặc biệt!");
        }
        if (user.getLastName().length() > 100) {
            errors.rejectValue("lastName", "user", "Họ không được dài quá 100 ký tự!");
        }
        if (user.getFirstName().length() > 50) {
            errors.rejectValue("firstName", "user", "Tên không được dài quá 100 ký tự!");
        }
        if (user.getEmail().length() > 100) {
            errors.rejectValue("email", "user", "Email không được dài quá 100 ký tự!");
        }
        if (userService.getUserByEmail(user.getEmail()) != null) {
            errors.rejectValue("email", "user", "Email đã được sử dụng!");
        }
        if (!user.getPhone().matches("\\d{10,}")) {
            errors.rejectValue("phone", "user", "Số điện thoại không hợp lệ!");
        }
        if (userService.getUserByPhone(user.getPhone()) != null) {
            errors.rejectValue("phone", "user", "Số điện thoại đã được sử dụng!");
        }
        if(errors.hasErrors())
            return "auth/signup";
        else {
            user.addRole(roleService.getRoleByID(1));

            userService.saveUser(user);
            
            redirectAttributes.addFlashAttribute("email",user.getEmail());

            return "redirect:/signup/success";
        }

    }
    @GetMapping("/signup/success")
    public String signupSuccess() {
        return "auth/signup_success";
    }
}
