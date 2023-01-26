package com.self.uaa.model;

import org.apache.commons.lang3.StringUtils;

public enum Role {
    ADMIN("ADMIN"), USER("USER"), ANONYMOUS("ANONYMOUS");

    private String value;

    Role(String value) {
        this.value = value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public Role getByValue(String value) {
        for (Role role: Role.values()) {
            if (StringUtils.equals(role.getValue(), value)) {
                return role;
            }
        }
        return Role.ANONYMOUS;
    }
}
