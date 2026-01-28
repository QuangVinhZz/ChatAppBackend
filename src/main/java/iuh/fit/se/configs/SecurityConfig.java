package iuh.fit.se.configs;

import iuh.fit.se.entities.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    CustomJwtDecoder customJwtDecoder;

    private final String[] POST_PUBLIC_ENDPOINT = {
            "/auth-management/api/v1/auth/log-in",
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // CORS
        httpSecurity.cors(cors -> cors.configurationSource(request -> {
            var corConfig = new CorsConfiguration();
            corConfig.addAllowedOrigin("http://localhost:5173");
            corConfig.addAllowedHeader("*");
            corConfig.addAllowedMethod("*");
            corConfig.setAllowCredentials(true);
            return corConfig;
        })).csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(request -> {
            request.requestMatchers(HttpMethod.POST, POST_PUBLIC_ENDPOINT).permitAll()
                    .anyRequest().authenticated();
        }).oauth2ResourceServer(oauth2 -> {
            oauth2.jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(new JwtAuthenticationEntrypoint())
                    .accessDeniedHandler(new CustomAccessDeniedHandler());
        });

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
