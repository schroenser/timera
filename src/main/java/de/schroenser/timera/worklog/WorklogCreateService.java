package de.schroenser.timera.worklog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.worklog.JiraWorklogService;

@Service
public class WorklogCreateService
{
    private final JiraWorklogService jiraWorklogService;
    private final String jiraBaseUrl;

    public WorklogCreateService(JiraWorklogService jiraWorklogService, @Value("${jira.base-url}") String jiraBaseUrl)
    {
        this.jiraWorklogService = jiraWorklogService;
        this.jiraBaseUrl = jiraBaseUrl;
    }

    public Worklog create(Worklog worklog)
    {
        return WorklogServices.applyModification(worklog,
            jiraWorklog -> jiraWorklogService.createWorklog(worklog.issueId(), jiraWorklog),
            jiraBaseUrl);
    }
}
