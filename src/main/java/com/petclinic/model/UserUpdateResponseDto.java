package com.petclinic.model;


public class UserUpdateResponseDto {

    private Long userId;
    private String username;

    public UserUpdateResponseDto(Long userId, String username) {
        this.userId = userId;
        this.username = username;
    }

    public UserUpdateResponseDto() {
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
