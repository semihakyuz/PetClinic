package com.petclinic.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;

import com.petclinic.domain.Role;
import com.petclinic.domain.User;
import com.petclinic.model.UserUpdateRequestDto;
import com.petclinic.model.UserUpdateResponseDto;
import com.petclinic.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService,
            BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    @GetMapping()
    public String getAllUsers(Model model) {

        return findPaginated(1, model);

    }

    @PostMapping("/registration")
    public String userRegister(@ModelAttribute("user") @Valid User user, BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (result.hasErrors()) {

            List<String> err = new ArrayList<>();
            result.getFieldErrors().forEach(e -> err.add(e.getDefaultMessage()));

            redirectAttributes.addFlashAttribute("userFieldsRegistrationErrors", err);
            redirectAttributes.addFlashAttribute("userRegistrationErrorUserObject", user);
            return "redirect:/user";

        }

        try {

            String userPassword = user.getPassword();
            Role userRole = new Role("ROLE_USER");

            user.setPassword(bCryptPasswordEncoder.encode(userPassword));
            user.setRoles(new HashSet<>(Arrays.asList(userRole)));

            userService.save(user);

            return "redirect:/user";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("userFieldsRegistrationErrors", "Kullanıcı adı zaten kayıtlı");
            return "redirect:/user";
        }

    }

    @PostMapping(value = "/delete")
    public String deleteUser(Long recordId, RedirectAttributes redirectAttributes

    ) {

        try {

            Long userId = recordId;

            Optional<User> existingUser = userService.findUserById(userId);

            if (existingUser.isPresent()) {

                boolean isAdmin = existingUser.get().getRoles()
                        .stream()
                        .anyMatch(r -> r.getRole().equals("ROLE_ADMIN"));

                if (isAdmin) {

                    redirectAttributes.addFlashAttribute("deleteAdminErrorMessage", "Admin kaydı silinemez.");
                    return "redirect:/user";
                }

                userService.deleteUserById(existingUser.get().getUserId());

                return "redirect:/user";

            }

            return "redirect:/user";

        } catch (Exception e) {

            return "redirect:/user";
        }

    }

    @GetMapping("/update")
    public String showUserProfileForm(@RequestParam(value = "userId", required = false) Long userId,
            Model model,
            Authentication auth) {

        User authUser = getUser(userId, auth);

        UserUpdateResponseDto response = new UserUpdateResponseDto();

        response.setUserId(authUser.getUserId());
        response.setUsername(authUser.getUsername());

        model.addAttribute("userInfo", response);
        return "home";
    }

    @PostMapping("/update")
    public String updateUserProfile(@RequestParam(value = "userId", required = false) Long userId,
            @ModelAttribute("userInfo") @Valid UserUpdateRequestDto request,
            BindingResult result, RedirectAttributes redirectAttributes, Authentication auth, Model model)
            throws ResponseStatusException {

        if (result.hasErrors()) {

            List<String> err = new ArrayList<>();
            result.getFieldErrors().forEach(e -> err.add(e.getDefaultMessage()));

            redirectAttributes.addFlashAttribute("userFieldsUpdateErrors", err);
            redirectAttributes.addFlashAttribute("userUpdateErrorsUserObject", request);
            return "redirect:/user/update";

        }

        try {

            User authUser = getUser(userId, auth);
            userService.updateUser(request, authUser);

            return "redirect:/logout";

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute("userFieldsUpdateErrors", "Kullanıcı adı zaten kayıtlı");
            return "redirect:/user/update";
        }

    }

    @GetMapping("/update/updatepassword")
    public String showUserPasswordForm(@RequestParam(value = "userId", required = false) Long userId,
            Model model,
            Authentication auth) {

        User authUser = getUser(userId, auth);

        Long authUserId = authUser.getUserId();
        String authUsername = authUser.getUsername();

        model.addAttribute("username", authUsername);
        model.addAttribute("userId", authUserId);
        return "home";
    }

    @PostMapping("/update/updatepassword")
    public String updateUserPassword(@RequestParam(value = "userId", required = false) Long userId, String password,
            Authentication auth, RedirectAttributes redirectAttributes)
            throws ResponseStatusException {

        User authUser = getUser(userId, auth);
        String newPassword = password;

        authUser.setPassword(bCryptPasswordEncoder.encode(newPassword));
        userService.updateUserPassword(authUser);

        return "redirect:/logout";
    }

    private User getUser(Long userId, Authentication auth) throws ResponseStatusException {

        if (userId == null) {

            User user = userService.findByUsername(auth.getName());

            return user;

        }

        Optional<User> user = userService.findUserById(userId);

        if (user.isEmpty()) {

            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Kullanıcı bulunamadı");
        }

        UserDetails userDetails = userService.loadUserByUsername(auth.getName());

        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {

            return user.get();
        }

        User authUser = userService.findByUsername(auth.getName());

        if (!authUser.equals(user.get())) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Yetkisiz işlem");
        }

        return user.get();
    }

    @GetMapping("/search")
    public String getSearchResult(@RequestParam(value = "searchterm", required = false) String searchterm, Model model,
            RedirectAttributes redirectAttributes) {

        if (searchterm == null) {

            return "redirect:/user";
        }
        ;

        List<User> searchResult = userService.searchUser(searchterm);

        if (searchResult.isEmpty()) {

            redirectAttributes.addFlashAttribute("notFoundUserMessage", searchterm);
            return "redirect:/user";

        }

        model.addAttribute("user", new User());
        model.addAttribute("foundedUserMessage",
                searchterm + "ile eşleşen" + searchResult.size() + "adet kayıt bulundu");
        model.addAttribute("allUsers", searchResult);
        return "home";

    }

    @GetMapping("/page/{pageNo}")
    public String findPaginated(@PathVariable(value = "pageNo") int pageNo, Model model) {

        int pageSize = 6;

        Page<User> page = userService.findPaginated(pageNo, pageSize);
        List<User> allUsers = page.getContent();

        model.addAttribute("currentPage", pageNo);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("totalUsers", page.getTotalElements());
        model.addAttribute("allUsers", allUsers);
        return "home";
    }

}
