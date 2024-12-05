package de.schroenser.timera.worklog;

import java.time.temporal.ChronoUnit;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.worklog.JiraWorklog;
import de.schroenser.timera.jira.worklog.JiraWorklogService;

@Service
@RequiredArgsConstructor
public class WorklogUpdateService
{
    private final JiraWorklogService jiraWorklogService;

    public Worklog update(Worklog worklog)
    {
        var jiraWorklog = createJiraWorklog(worklog);

        JiraWorklog jiraWorklogResult = jiraWorklogService.updateWorklog(worklog.issueId(),
            worklog.worklogId(),
            jiraWorklog);

        return combineWithUpdateResult(worklog, jiraWorklogResult);
    }

    private static JiraWorklog createJiraWorklog(Worklog worklog)
    {
        return new JiraWorklog(null,
            worklog.start(),
            (int) worklog.start()
                .until(worklog.end(), ChronoUnit.SECONDS),
            worklog.worklogComment(),
            null,
            null);
    }

    private static Worklog combineWithUpdateResult(Worklog worklog, JiraWorklog jiraWorklogResult)
    {
        return new Worklog(worklog.issueId(),
            jiraWorklogResult.id(),
            worklog.issueKey(),
            worklog.issueSummary(),
            jiraWorklogResult.started(),
            jiraWorklogResult.started()
                .plusSeconds(jiraWorklogResult.timeSpentSeconds()),
            jiraWorklogResult.comment());
    }
}
