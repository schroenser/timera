package de.schroenser.timera.jira.user;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
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
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(JiraCurrentUser.class);
    }
}
