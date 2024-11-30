package de.schroenser.timera.jira;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
class JiraConfiguration
{
    @Bean
    public JiraService jiraService(@Value("${jira.base-url}") String baseUrl, @Value("${jira.token}") String token)
    {
        var restClient = RestClient.builder()
            .baseUrl(baseUrl)
            .defaultHeader("User-Agent", "Timera/1.0 (Sven Haberer - DDS)")
            .defaultHeader("Authorization", "Bearer " + token)
            .build();
        var adapter = RestClientAdapter.create(restClient);
        var factory = HttpServiceProxyFactory.builderFor(adapter)
            .build();
        return factory.createClient(JiraService.class);
    }
}
