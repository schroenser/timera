package de.schroenser.timera.worklog;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.worklog.JiraWorklogService;

@Service
@RequiredArgsConstructor
public class WorklogDeleteService
{
    private final JiraWorklogService jiraWorklogService;

    public void delete(String issueId, String worklogId)
    {
        jiraWorklogService.deleteWorklog(issueId, worklogId);
    }
}
