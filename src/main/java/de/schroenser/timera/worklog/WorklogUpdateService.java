package de.schroenser.timera.worklog;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.worklog.JiraWorklogService;

@Service
@RequiredArgsConstructor
public class WorklogUpdateService
{
    private final JiraWorklogService jiraWorklogService;

    public Worklog update(Worklog worklog)
    {
        return WorklogServices.applyModification(worklog,
            jiraWorklog -> jiraWorklogService.updateWorklog(worklog.issueId(), worklog.worklogId(), jiraWorklog));
    }
}
