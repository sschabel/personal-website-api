package com.samschabel.pw.api.model.security;

public enum AuthorityEnum {

    ADMIN("ADMIN");

    private String value;

    AuthorityEnum(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
