package de.schroenser.timera.jira;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class JiraService
{
    private final RestClient restClient;

    public Stream<JiraIssue> streamIssues()
    {
        Function<Integer, IssueResponse> pageProvider = startAt -> getIssueResponse(new IssueSearchParameters(startAt));
        return StreamSupport.stream(new PagedResponseSpliterator<>(pageProvider), false);
    }

    private IssueResponse getIssueResponse(IssueSearchParameters issueSearchParameters)
    {
        return restClient.post()
            .uri("rest/api/2/search")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(issueSearchParameters)
            .retrieve()
            .body(IssueResponse.class);
    }

    @Cacheable("worklogs")
    public List<JiraWorklog> listWorklogs(String issueId, String updated)
    {
        return getWorklogResponse(issueId).worklogs();
    }

    private WorklogResponse getWorklogResponse(String issueId)
    {
        return restClient.get()
            .uri("rest/api/2/issue/{issueId}/worklog", issueId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(WorklogResponse.class);
    }

    public JiraCurrentUser getCurrentUser()
    {
        return restClient.get()
            .uri("rest/auth/1/session")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(JiraCurrentUser.class);
    }
}
