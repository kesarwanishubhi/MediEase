package com.shubhi.mediease.entity;

import java.util.Arrays;
import java.util.List;

public enum Role {
    ADMIN(Arrays.asList("CREATE", "UPDATE")),
    DOCTOR(Arrays.asList("UPDATE")),
    PATIENT(Arrays.asList("UPDATE"));

    private final List<String> permissions;

    Role(List<String> permissions) {
        this.permissions = permissions;
    }

    public List<String> getPermissions() {
        return permissions;
    }
}

