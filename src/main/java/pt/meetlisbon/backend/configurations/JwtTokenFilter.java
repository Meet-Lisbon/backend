package pt.meetlisbon.backend.configurations;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filters requests to find and validate JWT tokens
 * <p>
 * Spring security doesn't let us return a body or
 * change the status code from 401 {@link HttpStatus#FORBIDDEN}.
 */
@AllArgsConstructor
 public class JwtTokenFilter extends OncePerRequestFilter {
    private JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper mapper;
    @Override
    protected void doFilterInternal(@Nullable HttpServletRequest httpServletRequest,
                                    @Nullable HttpServletResponse httpServletResponse,
                                    @Nullable FilterChain filterChain) throws IOException {

            if(httpServletRequest == null
                    || httpServletResponse == null
                    || filterChain == null) return;

            String token = jwtTokenProvider.resolveToken(httpServletRequest);

            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        try {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } catch (ServletException | IOException e) {
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", e.toString());
//            errorDetails.put("message", "Invalid token");

            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);

            mapper.writeValue(httpServletResponse.getWriter(), errorDetails);
            }
        }
    }