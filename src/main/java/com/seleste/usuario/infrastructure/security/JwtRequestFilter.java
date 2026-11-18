package com.seleste.usuario.infrastructure.security;

import com.seleste.usuario.infrastructure.exceptions.DTO.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtRequestFilter(JwtUtil jwtUtil,
                            UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = req.getHeader("Authorization");

        if (authorizationHeader == null ||
                !authorizationHeader.startsWith("Bearer ")) {

            chain.doFilter(req, res);
            return;
        }
        try {

            final String token = authorizationHeader.substring(7);
            final String username = jwtUtil.extractUsername(token);

            if (username != null && SecurityContextHolder.getContext()
                    .getAuthentication() == null) {
                UserDetails userDetails = userDetailsService
                        .loadUserByUsername(username);
                if (jwtUtil.validateToken(token, username)) {
                    UsernamePasswordAuthenticationToken authentication = new
                            UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails
                            .getAuthorities());
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authentication);
                }
            }
            chain.doFilter(req, res);
        } catch (ExpiredJwtException e) {
           buildErrorResponse(req, res, e);
        }
    }

    private void buildErrorResponse(HttpServletRequest req,
                                    HttpServletResponse res,
                                    RuntimeException ex) throws IOException {
        ErrorResponseDTO errorDTO = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                "Unauthorized",
                "Token inválido ou expirado",
                req.getRequestURI()
        );
        ObjectMapper objectMapper = new ObjectMapper();
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(res.getWriter(), errorDTO);
    }
}
