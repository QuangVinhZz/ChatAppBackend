package iuh.fit.se.entities.enums;

import lombok.AccessLevel;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@ToString
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum AccountType {
    USERNAME("Username"),
    EMAIL("Email"),
    GOOGLE("Google");

    String enumType;

    AccountType(String enumType) {
        this.enumType = enumType;
    }
}
