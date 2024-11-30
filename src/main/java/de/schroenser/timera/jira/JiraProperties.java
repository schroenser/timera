package de.schroenser.timera.jira;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jira")
public record JiraProperties(String baseUrl, String token)
{
}
