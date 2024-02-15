package com.jwt.impl.utils;

import com.jwt.impl.rest.payload.request.SignUpRequest;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterValidation {

    public boolean validateUser(SignUpRequest response) {
        return isUsernameValid(response.username()) &&
                isPasswordValid(response.password()) &&
                isEmailValid(response.email()) &&
                isNameValid(response.firstName()) &&
                isMiddleNameValid(response.middleName()) &&
                isNameValid(response.lastName()) &&
                isAgeValid(response.age()) &&
                isPhoneNumberValid(response.phoneNumber());
    }

    public boolean isUsernameValid(String username) {
        return !username.isEmpty();
    }

    public boolean isPasswordValid(String password) {
        if (password.isEmpty())
            return false;

        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }

    private boolean isNameValid(String name) {
        if (name == null || name.isEmpty())
            return false;
        Pattern pattern = Pattern.compile("^[a-zA-Z]+$");
        Matcher matcher = pattern.matcher(name);

        return matcher.matches();
    }

    private boolean isEmailValid(String email) {
        Pattern pattern = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private boolean isMiddleNameValid(String middleName) {
        if (middleName == null || middleName.isEmpty())
            return true;
        Pattern pattern = Pattern.compile("^[a-zA-Z ]+$");
        Matcher matcher = pattern.matcher(middleName);

        return matcher.matches();
    }

    private boolean isAgeValid(Integer age) {
        return age > 16 && age < 120;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        Pattern pattern = Pattern.compile("^\\d{10}$");
        Matcher matcher = pattern.matcher(phoneNumber);

        return matcher.matches();
    }

}
