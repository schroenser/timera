package de.schroenser.timera.jira.issue;

import java.time.OffsetDateTime;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import de.schroenser.timera.jira.paged.PagedResponseSpliterator;

@Service
@RequiredArgsConstructor
public class JiraIssueService
{
    private final RestClient restClient;

    public Stream<JiraIssue> streamIssues(OffsetDateTime start)
    {
        Function<Integer, IssueResponse> pageProvider = startAt -> getIssueResponse(new IssueSearchParameters(start,
            startAt));
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
}
