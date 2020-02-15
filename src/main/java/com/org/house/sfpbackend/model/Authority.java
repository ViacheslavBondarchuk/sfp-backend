package com.org.house.sfpbackend.model;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    USER, ADMIN, OWNER;

    @Override
    public String getAuthority() {
        return name();
    }
}
