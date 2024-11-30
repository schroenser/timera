package de.schroenser.timera.worklog;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.IssueResponse;
import de.schroenser.timera.jira.IssueSearchParameters;
import de.schroenser.timera.jira.JiraIssue;
import de.schroenser.timera.jira.JiraService;
import de.schroenser.timera.jira.JiraWorklog;
import de.schroenser.timera.jira.PagedResponseSpliterator;

@Service
@RequiredArgsConstructor
public class WorklogService
{
    private final JiraService jiraService;

    public List<Worklog> list()
    {
        String currentUserName = jiraService.getCurrentUser()
            .name();

        return getJiraIssues().flatMap(jiraIssue -> getJiraWorklogs(jiraIssue).filter(jiraWorklog -> isFrom(jiraWorklog,
                    currentUserName))
                .map(jiraWorklog -> createWorklog(jiraIssue, jiraWorklog)))
            .toList();
    }

    private Stream<JiraIssue> getJiraIssues()
    {
        Function<Integer, IssueResponse> pageProvider = startAt -> jiraService.searchIssues(new IssueSearchParameters(
            startAt));
        return StreamSupport.stream(new PagedResponseSpliterator<>(pageProvider), false);
    }

    private Stream<JiraWorklog> getJiraWorklogs(JiraIssue jiraIssue)
    {
        return jiraService.getWorklogs(jiraIssue.id())
            .worklogs()
            .stream();
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
