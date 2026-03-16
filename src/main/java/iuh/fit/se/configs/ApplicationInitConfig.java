package iuh.fit.se.configs;

import iuh.fit.se.entities.AccountCredential;
import iuh.fit.se.entities.Role;
import iuh.fit.se.entities.User;
import iuh.fit.se.entities.enums.AccountType;
import iuh.fit.se.entities.enums.UserRole;
import iuh.fit.se.repositories.AccountCredentialRepository;
import iuh.fit.se.repositories.RoleRepository;
import iuh.fit.se.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${admin.username}")
    String adminUserName;

    @NonFinal
    @Value("${admin.password}")
    String adminPassword;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository,
                                        AccountCredentialRepository accountCredentialRepository,
                                        RoleRepository roleRepository) {
        return args -> {
            if (accountCredentialRepository.findByCredential(adminUserName) == null) {
                Role adminRole = roleRepository.findById(UserRole.ADMIN.name()).orElse(null);
                if (adminRole == null)
                    throw new NullPointerException("Admin role not found!");
                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);

                // Sử dụng User thay vì Employee
                User adminUser = User.builder()
                        .roles(roles)
                        .firstName("Admin")
                        .lastName("System")
                        .build();

                userRepository.save(adminUser);

                AccountCredential accountCredential = AccountCredential.builder()
                        .credential(adminUserName)
                        .password(passwordEncoder.encode(adminPassword))
                        .type(AccountType.USERNAME)
                        .user(adminUser)
                        .isVerified(true)
                        .build();
                accountCredentialRepository.save(accountCredential);

                log.warn("admin has been created with username: " + adminUserName + ", password: " + adminPassword);
            }
        };
    }
}