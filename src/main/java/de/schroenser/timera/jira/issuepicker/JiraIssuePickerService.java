package de.schroenser.timera.jira.issuepicker;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class JiraIssuePickerService
{
    private final RestClient restClient;

    public List<String> searchIssuePickerKeys(String query)
    {
        return restClient.get()
            .uri("rest/api/2/issue/picker?query={query}", query)
            .retrieve()
            .body(JiraIssuePickerResult.class)
            .sections()
            .getFirst()
            .issues()
            .stream()
            .map(JiraIssuePickerIssue::key)
            .toList();
    }
}
