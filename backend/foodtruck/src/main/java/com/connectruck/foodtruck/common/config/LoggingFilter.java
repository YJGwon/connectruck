package com.connectruck.foodtruck.common.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@WebFilter(urlPatterns = "/api/*")
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    private static final String FORMAT_REQUEST = "request processed - uri: {}, method: {}";
    private static final String FORMAT_PARAMS = ", parameters: {}";
    private static final String FORMAT_RESPONSE = "response returned - statusCode: {}, duration: {}ms";
    private static final String FORMAT_BODY = "\nbody: {}";

    private static final String FORMAT_PARAM = "%s=%s";
    private static final String DELIMITER_PARAM_VALUES = ",";
    private static final String DELIMITER_PARAMS = "&";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        final ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        final ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);
        final long startTime = System.currentTimeMillis();

        MDC.put("traceId", UUID.randomUUID().toString());

        filterChain.doFilter(wrappedRequest, wrappedResponse); // process request

        logRequest(wrappedRequest);
        logResponse(wrappedResponse, System.currentTimeMillis() - startTime);
        MDC.clear();
        wrappedResponse.copyBodyToResponse();
    }

    private void logRequest(final ContentCachingRequestWrapper request) {
        final String uri = request.getRequestURI();
        final String method = request.getMethod();
        final Map<String, String[]> parameters = request.getParameterMap();
        final String body = new String(request.getContentAsByteArray());

        if (parameters.isEmpty() && body.isBlank()) {
            log.info(FORMAT_REQUEST, uri, method);
            return;
        }

        if (!parameters.isEmpty() && body.isBlank()) {
            log.info(FORMAT_REQUEST + FORMAT_PARAMS, uri, method, toQueryString(parameters));
            return;
        }

        if (parameters.isEmpty() && !body.isBlank()) {
            log.info(FORMAT_REQUEST + FORMAT_BODY, uri, method, body);
            return;
        }

        log.info(FORMAT_REQUEST + FORMAT_PARAMS + FORMAT_BODY, uri, method, toQueryString(parameters), body);
    }

    private String toQueryString(final Map<String, String[]> parameters) {
        return parameters.entrySet().stream()
                .map(entry -> String.format(FORMAT_PARAM,
                        entry.getKey(),
                        String.join(DELIMITER_PARAM_VALUES, entry.getValue())))
                .collect(Collectors.joining(DELIMITER_PARAMS));
    }

    private void logResponse(final ContentCachingResponseWrapper response, final long duration) {
        final int status = response.getStatus();
        final String body = new String(response.getContentAsByteArray());

        if (body.isBlank()) {
            log.info(FORMAT_RESPONSE, status, duration);
            return;
        }

        log.info(FORMAT_RESPONSE + FORMAT_BODY, status, duration, body);
    }
}
