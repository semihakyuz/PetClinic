package com.petclinic.model;

import javax.validation.constraints.NotEmpty;

public class UserUpdateRequestDto {

    private Long userId;
    @NotEmpty(message = "Kullanıcı adı boş olamaz")
    private String username;

    public UserUpdateRequestDto(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UserUpdateRequestDto() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "UserInfoUpdateDto [userId=" + userId + ", username=" + username + "]";
    }

}
