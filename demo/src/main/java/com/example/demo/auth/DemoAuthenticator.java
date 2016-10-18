package com.example.demo.auth;

import com.example.demo.auth.ImmutableUser;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

import java.util.Optional;

public class DemoAuthenticator implements Authenticator<BasicCredentials, User> {
    @Override
    public Optional<User> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if ("secret".equals(credentials.getPassword())) {
            final User user = ImmutableUser.builder()
                    .name(credentials.getUsername())
                    .build();
            return Optional.of(user);
        }
        return Optional.empty();
    }
}
