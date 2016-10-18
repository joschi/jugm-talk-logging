package com.example.demo.auth;

import io.dropwizard.auth.Authorizer;

public class DemoAuthorizer implements Authorizer<User> {
    private final String userName;

    public DemoAuthorizer(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean authorize(User user, String role) {
        return user.getName().equals(userName) && role.equals("admin");
    }
}
