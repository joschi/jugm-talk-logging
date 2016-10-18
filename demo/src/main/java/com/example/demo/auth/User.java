package com.example.demo.auth;

import com.example.demo.auth.ImmutableUser;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.hibernate.validator.constraints.NotEmpty;
import org.immutables.value.Value;

import java.security.Principal;

@Value.Immutable
@JsonSerialize(as = ImmutableUser.class)
@JsonDeserialize(as = ImmutableUser.class)
public interface User extends Principal {
    @NotEmpty
    String name();

    @Override
    default String getName() {
        return name();
    }
}
