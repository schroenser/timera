package de.schroenser.timera.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Slf4j
public class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor
{
    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
        throws IOException
    {
        logRequest(request, body);
        var response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    private void logRequest(HttpRequest request, byte[] body)
    {
        log.info("Request: {} {}", request.getMethod(), request.getURI());
        logHeaders("Request", request.getHeaders());
        if (body != null && body.length > 0)
        {
            log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
        }
    }

    private void logResponse(ClientHttpResponse response) throws IOException
    {
        log.info("Response status: {}", response.getStatusCode());
        logHeaders("Response", response.getHeaders());
    }

    private void logHeaders(String type, HttpHeaders headers)
    {
        log.info("{} Headers:", type);
        headers.forEach((name, value) -> log.info("    {}: {}", name, value));
    }
}
