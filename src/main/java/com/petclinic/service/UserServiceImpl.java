package com.petclinic.service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import com.petclinic.domain.Role;
import com.petclinic.domain.User;
import com.petclinic.model.UserUpdateRequestDto;
import com.petclinic.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public Optional<User> findUserById(Long userId) {

        return userRepository.findById(userId);
    }

    public User findByEmail(String email) {

        return userRepository.findByUsername(email);
    }

    public User findByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    public User save(User user) {

        return userRepository.save(user);
    }

    public List<User> searchUser(String username) {

        return userRepository.findByUsernameContaining(username);
    }

    public User updateUser(UserUpdateRequestDto newUserInfo, User authUserInfo) {

        authUserInfo.setUsername(newUserInfo.getUsername());

        return userRepository.save(authUserInfo);
    }

    public void updateUserPassword(User modifiedUser) {

        userRepository.save(modifiedUser);
    }

    public void deleteUserById(Long userId) {
        userRepository.deleteById(userId);
    }

    public Page<User> findPaginated(int pageNo, int pageSize) {

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);

        return userRepository.findAll(pageable);
    }

    public Long userCount() {

        return userRepository.count();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {

            throw new UsernameNotFoundException("Invalid email");
        }

        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles) {

        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }

    public boolean isAdmin(Authentication authentication) {

        return authentication
                .getAuthorities()
                .stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority()
                        .equals("ADMIN"));
    }

}
