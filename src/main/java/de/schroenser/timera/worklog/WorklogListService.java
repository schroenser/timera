package de.schroenser.timera.worklog;

import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.issue.JiraIssue;
import de.schroenser.timera.jira.issue.JiraIssueService;
import de.schroenser.timera.jira.user.JiraUserService;
import de.schroenser.timera.jira.worklog.JiraWorklog;
import de.schroenser.timera.jira.worklog.JiraWorklogService;

@Service
public class WorklogListService
{
    private final JiraUserService jiraUserService;
    private final JiraIssueService jiraIssueService;
    private final JiraWorklogService jiraWorklogService;
    private final String jiraBaseUrl;

    public WorklogListService(
        JiraUserService jiraUserService,
        JiraIssueService jiraIssueService,
        JiraWorklogService jiraWorklogService,
        @Value("${jira.base-url}") String jiraBaseUrl)
    {
        this.jiraUserService = jiraUserService;
        this.jiraIssueService = jiraIssueService;
        this.jiraWorklogService = jiraWorklogService;
        this.jiraBaseUrl = jiraBaseUrl;
    }

    public List<Worklog> list(OffsetDateTime start, OffsetDateTime end)
    {
        String currentUserName = jiraUserService.getCurrentUser()
            .name();

        return jiraIssueService.getUpdatedIssuesWithWorklogs(start)
            .stream()
            .flatMap(jiraIssue -> jiraWorklogService.listWorklogs(jiraIssue.id(),
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

    private boolean isFrom(JiraWorklog jiraWorklog, String currentUserName)
    {
        return jiraWorklog.author()
            .name()
            .equals(currentUserName) ||
            jiraWorklog.updateAuthor()
                .name()
                .equals(currentUserName);
    }

    private Worklog createWorklog(JiraIssue jiraIssue, JiraWorklog jiraWorklog)
    {
        return new Worklog(jiraIssue.id(),
            jiraWorklog.id(),
            jiraIssue.key(),
            jiraIssue.fields()
                .summary(),
            jiraWorklog.started(),
            jiraWorklog.started()
                .plusSeconds(jiraWorklog.timeSpentSeconds()),
            jiraWorklog.comment(),
            jiraBaseUrl);
    }
}
