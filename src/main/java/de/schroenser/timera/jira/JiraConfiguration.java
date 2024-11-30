package de.schroenser.timera.jira;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
@EnableCaching
class JiraConfiguration
{
    @Bean
    public RestClient restClient(@Value("${jira.base-url}") String baseUrl, @Value("${jira.token}") String token)
    {
        return RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("User-Agent", "Timera/1.0 (Sven Haberer - DDS)")
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
    }
}
