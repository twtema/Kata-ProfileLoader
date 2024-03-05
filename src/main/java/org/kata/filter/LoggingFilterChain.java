package org.kata.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.function.Function;

@Slf4j
@Component
public class LoggingFilterChain implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        // Логирование входящего запроса и его заголовков
        log.info("Входящий запрос {} : {}", request.getMethod(), request.getRequestURI());
        logHeaders("Заголовки запросов: ", request.getHeaderNames(), request::getHeader);

        filterChain.doFilter(servletRequest, responseWrapper);

        // Логирование исходящего ответа и его заголовков
        log.info("Исходящий ответ : HTTP {}", responseWrapper.getStatus());
        logHeaders(responseWrapper.getHeaderNames(), responseWrapper::getHeaders);

        responseWrapper.copyBodyToResponse(); // Обязательно, чтобы передать содержимое тела ответа клиенту
    }

    private void logHeaders(String prefix, Enumeration<String> headerNames, Function<String, String> headerValueSupplier) {
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = headerValueSupplier.apply(headerName);
            log.info("{} {} = {}", prefix, headerName, headerValue);
        }
    }
    private void logHeaders(Collection<String> headerNames, Function<String, Collection<String>> headerValueSupplier) {
        for (String headerName : headerNames) {
            Collection<String> headerValues = headerValueSupplier.apply(headerName);
            log.info("Header: {} - Values: {}", headerName, headerValues);
        }
    }


}
