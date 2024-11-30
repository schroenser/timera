package de.schroenser.timera.worklog;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.JiraIssue;
import de.schroenser.timera.jira.JiraService;
import de.schroenser.timera.jira.JiraWorklog;

@Service
@RequiredArgsConstructor
public class WorklogService
{
    private final JiraService jiraService;

    public List<Worklog> list()
    {
        String currentUserName = jiraService.getCurrentUser()
            .name();

        return jiraService.streamIssues()
            .flatMap(jiraIssue -> jiraService.listWorklogs(jiraIssue.id(),
                    jiraIssue.fields()
                        .updated())
                .stream()
                .filter(jiraWorklog -> isFrom(jiraWorklog, currentUserName))
                .map(jiraWorklog -> createWorklog(jiraIssue, jiraWorklog)))
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
            jiraWorklog.timeSpentSeconds(),
            jiraWorklog.comment());
    }
}
