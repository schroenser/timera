package de.schroenser.timera.worklog;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.worklog.JiraWorklogService;

@Service
@RequiredArgsConstructor
public class WorklogCreateService
{
    private final JiraWorklogService jiraWorklogService;

    public Worklog create(Worklog worklog)
    {
        return WorklogServices.applyModification(worklog,
            jiraWorklog -> jiraWorklogService.createWorklog(worklog.issueId(), jiraWorklog));
    }
}
