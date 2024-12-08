package de.schroenser.timera.jira.worklog;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class JiraWorklogService
{
    private final RestClient restClient;

    @Cacheable("worklogs")
    public List<JiraWorklog> listWorklogs(String issueId, OffsetDateTime updated)
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

    public JiraWorklog createWorklog(String issueId, JiraWorklog worklog)
    {
        return restClient.post()
            .uri("rest/api/2/issue/{issueId}/worklog?adjustEstimate=leave", issueId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(worklog)
            .retrieve()
            .body(JiraWorklog.class);
    }

    public JiraWorklog updateWorklog(String issueId, String worklogId, JiraWorklog worklog)
    {
        return restClient.put()
            .uri("rest/api/2/issue/{issueId}/worklog/{worklogId}?adjustEstimate=leave", issueId, worklogId)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(worklog)
            .retrieve()
            .body(JiraWorklog.class);
    }
}
