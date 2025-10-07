package org.city.alert.alert.service.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum UserRoles {
    CITIZEN,
    TECHNICIAN,
    ADMIN;

    @JsonCreator
    public static UserRoles fromString(String key) {
        return key == null
                ? null
                : UserRoles.valueOf(key.toUpperCase());
    }
}
