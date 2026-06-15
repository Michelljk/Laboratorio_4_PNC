package com.server.app.filters;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.app.dto.response.ExceptionResponse;
import com.server.app.entities.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class DynamicAuthorizationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            String method = request.getMethod();
            String path = request.getRequestURI();

            boolean hasPermission = user.getRole().getPermissions().stream()
                    .anyMatch(p -> p.getMethod().equalsIgnoreCase(method) && path.startsWith(p.getPath()));

            if (!hasPermission && !path.startsWith("/api/auth")) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                ExceptionResponse exceptionResponse = new ExceptionResponse(HttpServletResponse.SC_FORBIDDEN, "No tienes permiso para acceder a este recurso");
                response.getWriter().write(new ObjectMapper().writeValueAsString(exceptionResponse));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
