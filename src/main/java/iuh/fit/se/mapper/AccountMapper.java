package iuh.fit.se.mapper;

import iuh.fit.se.dtos.response.AccountCredentialResponse;
import iuh.fit.se.entities.AccountCredential;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountCredentialResponse toAccountCredentialResponse(AccountCredential accountCredential);
    @AfterMapping
    default void setAccountCredentialResponseDefaults(@MappingTarget AccountCredentialResponse accountCredentialResponse,
                                                      AccountCredential accountCredential){
//        accountCredentialResponse.setUserId(accountCredential.getUser().getId());
    }
}
