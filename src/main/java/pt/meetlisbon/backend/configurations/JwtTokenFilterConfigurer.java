package pt.meetlisbon.backend.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configures and applies the {@link JwtTokenFilter}
 *
 * This filter prevents access to all unauthenticated users on most
 * endpoints, responding with {@link org.springframework.http.HttpStatus#FORBIDDEN}
 * @see pt.meetlisbon.backend.controllers.AuthController
 */
@RequiredArgsConstructor
public class JwtTokenFilterConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper mapper;
    @Override
    public void configure(HttpSecurity http) {
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter(jwtTokenProvider, mapper);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
    }

}