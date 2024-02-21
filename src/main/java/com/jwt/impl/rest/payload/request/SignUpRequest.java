package com.jwt.impl.rest.payload.request;

public record SignUpRequest(
        String username,
        String firstName,
        String middleName,
        String lastName,
        Integer age,
        String email,
        String password,
        String courseName,
        String phoneNumber

) {
}
