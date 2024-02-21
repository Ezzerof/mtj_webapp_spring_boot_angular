package com.jwt.impl.rest.payload.request;

public class ChangePasswordRequest {
    private String currentPassword;
    private String newPassword;
    private String email;

    public ChangePasswordRequest(String currentPassword, String newPassword, String email) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.email = email;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
