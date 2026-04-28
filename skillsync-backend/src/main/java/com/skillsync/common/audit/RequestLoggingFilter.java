package com.skillsync.common.audit;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Logs every incoming HTTP request with method, URI, status and duration.
 *
 * Routed to logs/skillsync.log via the com.skillsync.backend.common.audit
 * logger defined in logback-spring.xml.
 *
 * Requests taking longer than SLOW_REQUEST_THRESHOLD_MS are logged as WARN
 * so they stand out in the main log and get captured in the error log too.
 */
@Component
public class RequestLoggingFilter implements Filter {

    private static final Logger logger =
            LoggerFactory.getLogger(RequestLoggingFilter.class);

    // Requests slower than this are flagged as WARN
    private static final long SLOW_REQUEST_THRESHOLD_MS = 2000;

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  httpRequest  = (HttpServletRequest)  request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        long start = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - start;
            int  status   = httpResponse.getStatus();

            String message = "{} {} → {} | {}ms";

            if (duration > SLOW_REQUEST_THRESHOLD_MS) {
                // Slow request — logged as WARN so it appears in error log too
                logger.warn("SLOW REQUEST " + message,
                        httpRequest.getMethod(),
                        httpRequest.getRequestURI(),
                        status, duration);

            } else if (status >= 500) {
                // Server error
                logger.error(message,
                        httpRequest.getMethod(),
                        httpRequest.getRequestURI(),
                        status, duration);

            } else if (status >= 400) {
                // Client error
                logger.warn(message,
                        httpRequest.getMethod(),
                        httpRequest.getRequestURI(),
                        status, duration);

            } else {
                // Normal request
                logger.info(message,
                        httpRequest.getMethod(),
                        httpRequest.getRequestURI(),
                        status, duration);
            }
        }
    }
}