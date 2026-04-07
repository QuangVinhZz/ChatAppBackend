package iuh.fit.se.configs;

import iuh.fit.se.entities.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomJwtDecoder customJwtDecoder;

    private final String[] POST_PUBLIC_ENDPOINT = {
            "/auth-management/api/v1/auth/log-in",
            "/auth-management/api/v1/auth/refresh",
            "/auth-management/api/v1/auth/logout",
            "/api/v1/users/register",
            "/api/v1/users/send-otp" 
    };

    private final String[] GET_PUBLIC_ENDPOINT = {
            "/avatars/**"
    };

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // CORS
        httpSecurity.cors(cors -> cors.configurationSource(request -> {
            var corConfig = new CorsConfiguration();
            corConfig.addAllowedOrigin("*");
            corConfig.addAllowedOrigin("http://192.168.1.34:3000");
            corConfig.addAllowedHeader("*");
            corConfig.addAllowedMethod("*");
            corConfig.setAllowCredentials(false);
            return corConfig;
        })).csrf(AbstractHttpConfigurer::disable);

        httpSecurity.authorizeHttpRequests(request -> {
            request.requestMatchers(HttpMethod.POST, POST_PUBLIC_ENDPOINT).permitAll()
                    .requestMatchers(HttpMethod.GET, GET_PUBLIC_ENDPOINT).permitAll() // Thêm dòng này để cho phép GET ảnh
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
}