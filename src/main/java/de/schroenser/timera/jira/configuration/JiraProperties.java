package de.schroenser.timera.jira.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jira")
public record JiraProperties(String baseUrl, String token, boolean logRequests)
{
}
