package com.ngt.cuoiky.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ngt.cuoiky.model.Address;
import com.ngt.cuoiky.model.Cart;
import com.ngt.cuoiky.model.District;
import com.ngt.cuoiky.model.Province;
import com.ngt.cuoiky.model.User;
import com.ngt.cuoiky.model.Ward;
import com.ngt.cuoiky.principal.UserPrincipal;
import com.ngt.cuoiky.service.AddressService;
import com.ngt.cuoiky.service.CartService;
import com.ngt.cuoiky.service.UserService;

@Controller
public class ProfileController {

    private final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private AddressService addressService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CartService cartService;

    @GetMapping("/profile/info")
    public String profileInfo(@AuthenticationPrincipal UserPrincipal loggedUser, Model model,
                              RedirectAttributes redirectAttributes) {
        Integer id = loggedUser.getId();
        try {
            User user = userService.getUserByID(id);
            List<Cart> listCarts = cartService.findCartByUser(loggedUser.getId());

            double estimatedTotal = 0;

            for (Cart item : listCarts) {
                estimatedTotal += item.getSubtotal();
            }

            model.addAttribute("listCarts", listCarts);
            model.addAttribute("estimatedTotal", estimatedTotal);
            model.addAttribute("user", user);
            return "profile-user/profile";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "profile-user/profile";
        }

    }

    @GetMapping("/profile/edit")
    public String profileEdit(@AuthenticationPrincipal UserPrincipal loggedUser, Model model,
                              RedirectAttributes redirectAttributes) {
        Integer id = loggedUser.getId();
        try {
            List<Cart> listCarts = cartService.findCartByUser(loggedUser.getId());

            double estimatedTotal = 0;

            for (Cart item : listCarts) {
                estimatedTotal += item.getSubtotal();
            }

            model.addAttribute("listCarts", listCarts);
            model.addAttribute("estimatedTotal", estimatedTotal);
            if (!model.containsAttribute("user")) {
                User user = userService.getUserByID(id);
                model.addAttribute("user", user);
            }
            return "profile-user/edit-profile";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "profile-user/edit-profile";
        }
    }

    @PostMapping("/profile/edit")
    public String editProfile(User user, BindingResult errors, @AuthenticationPrincipal UserPrincipal loggedUser,
                              RedirectAttributes redirectAttributes) {
        Integer idLoggedUser = loggedUser.getId();
        if(!idLoggedUser.equals(user.getId())) {
            redirectAttributes.addFlashAttribute("messageError", "Loi xac thuc nguoi dung!");
            return "redirect:/profile/edit";
        }
        try {
            User existUser = userService.getUserByID(user.getId());
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
            if (!user.getPhone().matches("\\d{10,}")) {
                errors.rejectValue("phone", "user", "Số điện thoại không hợp lệ!");
            }
            if ((userService.getUserByPhone(user.getPhone()) != null) && !user.getPhone().equals(existUser.getPhone())) {
                errors.rejectValue("phone", "user", "Số điện thoại đã được sử dụng!");
            }
            if (errors.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.user", errors);
                redirectAttributes.addFlashAttribute("user", user);
                return "redirect:/profile/edit";
            } else{
                existUser.setFirstName(user.getFirstName());
                existUser.setLastName(user.getLastName());
                existUser.setPhone(user.getPhone());
                log.info(existUser.toString());
                userService.saveEditUser(existUser);
                redirectAttributes.addFlashAttribute("messageSuccess", "Người dùng đã được chỉnh sửa thành công.");
                return "redirect:/profile/edit";
            }
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/profile/edit";
        }

    }

    @GetMapping("/profile/address")
    public String getListAddress(@AuthenticationPrincipal UserPrincipal loggedUser, Model model,
                                 RedirectAttributes redirectAttributes) {
        Integer id = loggedUser.getId();
        try {
            // default listcart 
            List<Cart> listCarts = cartService.findCartByUser(id);
            double estimatedTotal = 0;

            for (Cart item : listCarts) {
                estimatedTotal += item.getSubtotal();

            }
            model.addAttribute("listCarts", listCarts);
            model.addAttribute("estimatedTotal", estimatedTotal);

            User user = userService.getUserByID(id);
            List<Address> listAddresses = addressService.getListAddressByUserId(id);

            model.addAttribute("listAddresses", listAddresses);
            model.addAttribute("user", user);
            return "profile-user/address";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "profile-user/address";
        }
    }
    @GetMapping("/profile/address/add")
    public String newAddress(@AuthenticationPrincipal UserPrincipal loggedUser, Model model,
                                 RedirectAttributes redirectAttributes) {
        Integer id = loggedUser.getId();
        try {
            User user = userService.getUserByID(id);
            List<Cart> listCarts = cartService.findCartByUser(id);
            double estimatedTotal = 0;

            for (Cart item : listCarts) {
                estimatedTotal += item.getSubtotal();

            }
            model.addAttribute("listCarts", listCarts);
            model.addAttribute("estimatedTotal", estimatedTotal);
            List<Province> listProvinces = addressService.getListProvinces();
            List<District> listDistricts = addressService.getListDistrict();
            List<Ward> listWards = addressService.getListWard();



            model.addAttribute("listDistricts", listDistricts);
            model.addAttribute("listWards", listWards);
            model.addAttribute("listProvinces", listProvinces);
            model.addAttribute("user", user);
            model.addAttribute("isEdit", false);
            //if address is not exist then create new address
            if (!model.containsAttribute("address")) {
                Address address = new Address();
                model.addAttribute("address", address);
            }

            return "profile-user/edit-address";
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "profile-user/edit-address";
        }
    }

    @PostMapping("/profile/address/add")
    public String saveAddress(Address address, BindingResult errors, @AuthenticationPrincipal UserPrincipal loggedUser,
                              RedirectAttributes redirectAttributes, Model model) {
        Integer id = loggedUser.getId();

        try {
            User user = userService.getUserByID(id);
            if (address.getLastName().matches(".*\\d+.*")) {
                errors.rejectValue("lastName", "address", "Họ không được chứa số!");
            }
            if (address.getLastName().matches(".*[:;/{}*<>=()!.#$@_+,?-]+.*")) {
                errors.rejectValue("lastName", "address", "Họ không được chứa ký tự đặc biệt!");
            }
            if (address.getFirstName().matches(".*\\d+.*")) {
                errors.rejectValue("firstName", "address", "Tên không được chứa số!");
            }
            if (address.getFirstName().matches(".*[:;/{}*<>=()!.#$@_+,?-]+.*")) {
                errors.rejectValue("firstName", "address", "Tên không được chứa ký tự đặc biệt!");
            }
            if (address.getLastName().length() > 100) {
                errors.rejectValue("lastName", "address", "Họ không được dài quá 100 ký tự!");
            }
            if (address.getFirstName().length() > 50) {
                errors.rejectValue("firstName", "address", "Tên không được dài quá 100 ký tự!");
            }
            if (address.getEmail().length() > 100) {
                errors.rejectValue("email", "address", "Email không được dài quá 100 ký tự!");
            }
            if (!address.getPhone().matches("\\d{10,}")) {
                errors.rejectValue("phone", "address", "Số điện thoại không hợp lệ!");
            }
            if(address.getSpecificAddress().length() > 200) {
                errors.rejectValue("lastName", "address", "Địa chỉ cụ thể không được dài quá 200 ký tự!");
            }
            if(errors.hasErrors()) {
                // return to form if has error, redirectAttributes to GetMapping
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.address", errors);
                redirectAttributes.addFlashAttribute("address", address);

                return "redirect:/profile/address/add";

            }
            else {
                address.setUser(user);
                log.info(address.toString());

                long numberAddressExist = addressService.getCountAddressByUserId(id);

                // If user doesn't have any address then set default as this address
                if(numberAddressExist == 0) {
                    address.setDefault(true);
                }
                addressService.save(address);
                redirectAttributes.addFlashAttribute("messageSuccess", "Địa chỉ đã được lưu thành công.");
                return "redirect:/profile/address";
            }
        }
        catch (Exception e) {
            redirectAttributes.addFlashAttribute("messageError", e.getMessage());
            return "redirect:/profile/address";
        }
    }
}
