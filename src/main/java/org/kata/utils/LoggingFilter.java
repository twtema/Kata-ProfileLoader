package org.kata.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "LoggingFilter", urlPatterns = "/*")
public class LoggingFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("REQUEST DATA: " + request.getRequestURI());
        log.info("RESPONSE DATA: " + response.getStatus());
        filterChain.doFilter(request, response);
    }
}
