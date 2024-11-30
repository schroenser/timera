package de.schroenser.timera.jira;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange(url = "rest")
public interface JiraService
{
    @PostExchange(url = "api/2/search", contentType = MediaType.APPLICATION_JSON_VALUE)
    IssueResponse searchIssues(@RequestBody IssueSearchParameters issueSearchParameters);

    @GetExchange(url = "api/2/issue/{issueId}/worklog")
    WorklogResponse getWorklogs(@PathVariable String issueId);

    @GetExchange(url = "auth/1/session")
    JiraCurrentUser getCurrentUser();
}
