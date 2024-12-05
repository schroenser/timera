package de.schroenser.timera.worklog;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.JiraService;
import de.schroenser.timera.jira.user.JiraUserService;
import de.schroenser.timera.jira.JiraWorklog;
import de.schroenser.timera.jira.issue.JiraIssue;
import de.schroenser.timera.jira.issue.JiraIssueService;

@Service
@RequiredArgsConstructor
public class WorklogService
{
    private final JiraService jiraService;
    private final JiraUserService jiraUserService;
    private final JiraIssueService jiraIssueService;

    public List<Worklog> list(OffsetDateTime start, OffsetDateTime end)
    {
        String currentUserName = jiraUserService.getCurrentUser()
            .name();

        return jiraIssueService.streamIssues(start)
            .flatMap(jiraIssue -> jiraService.listWorklogs(jiraIssue.id(),
                    jiraIssue.fields()
                        .updated())
                .stream()
                .filter(jiraWorklog -> isFrom(jiraWorklog, currentUserName))
                .map(jiraWorklog -> createWorklog(jiraIssue, jiraWorklog)))
            .filter(worklog -> worklog.end()
                .isAfter(start) &&
                worklog.start()
                    .isBefore(end))
            .toList();
    }

    private static boolean isFrom(JiraWorklog jiraWorklog, String currentUserName)
    {
        return jiraWorklog.author()
            .name()
            .equals(currentUserName) ||
            jiraWorklog.updateAuthor()
                .name()
                .equals(currentUserName);
    }

    private static Worklog createWorklog(JiraIssue jiraIssue, JiraWorklog jiraWorklog)
    {
        return new Worklog(jiraIssue.id(),
            jiraWorklog.id(),
            jiraIssue.key(),
            jiraIssue.fields()
                .summary(),
            jiraWorklog.started(),
            jiraWorklog.started()
                .plusSeconds(jiraWorklog.timeSpentSeconds()),
            jiraWorklog.comment());
    }

    public Worklog update(Worklog worklog)
    {
        var jiraWorklog = new JiraWorklog(null,
            worklog.start(),
            (int) worklog.start()
                .until(worklog.end(), ChronoUnit.SECONDS),
            worklog.worklogComment(),
            null,
            null);

        JiraWorklog jiraWorklogResult = jiraService.updateWorklog(worklog.issueId(), worklog.worklogId(), jiraWorklog);

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
