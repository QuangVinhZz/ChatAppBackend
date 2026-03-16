package iuh.fit.se.entities.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum TokenType {
    BEARER("Bearer");

    String tokenType;
    TokenType(String tokenType){
        this.tokenType = tokenType;
    }
}
