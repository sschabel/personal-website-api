package com.samschabel.pw.api.model.security;

public enum RoleEnum {

    ADMIN("ADMIN");

    private String value;

    RoleEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
