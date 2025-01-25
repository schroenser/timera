package de.schroenser.timera.jira.user;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class JiraUserService
{
    private final RestClient restClient;

    @Cacheable("currentUser")
    public JiraCurrentUser getCurrentUser()
    {
        return restClient.get()
            .uri("rest/auth/1/session")
            .retrieve()
            .body(JiraCurrentUser.class);
    }
}
