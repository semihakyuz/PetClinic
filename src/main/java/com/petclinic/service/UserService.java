package com.petclinic.service;

import java.util.List;
import java.util.Optional;
import com.petclinic.domain.User;
import com.petclinic.model.UserUpdateRequestDto;

import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    public User save(User user);

    public User findByEmail(String email);

    public User findByUsername(String username);

    public boolean isAdmin(Authentication authentication);

    public List<User> getAllUsers();

    public Page<User> findPaginated(int pageNo, int pageSize);

    public Optional<User> findUserById(Long userId);

    public void deleteUserById(Long userId);

    public User updateUser(UserUpdateRequestDto newUserInfo, User authUserInfo);

    public void updateUserPassword(User modifiedUser);

    public List<User> searchUser(String username);

    public Long userCount();
}
