package de.schroenser.timera.worklog;

import java.time.temporal.ChronoUnit;
import java.util.function.UnaryOperator;

import lombok.experimental.UtilityClass;

import de.schroenser.timera.jira.worklog.JiraWorklog;

@UtilityClass
class WorklogServices
{
    public Worklog applyModification(Worklog worklog, UnaryOperator<JiraWorklog> modifier, String jiraBaseUrl)
    {
        return enhanceJiraWorklog(modifier.apply(extractJiraWorklog(worklog)), worklog, jiraBaseUrl);
    }

    private static JiraWorklog extractJiraWorklog(Worklog worklog)
    {
        return new JiraWorklog(worklog.worklogId(),
            worklog.start(),
            (int) worklog.start()
                .until(worklog.end(), ChronoUnit.SECONDS),
            worklog.worklogComment(),
            null,
            null);
    }

    private static Worklog enhanceJiraWorklog(JiraWorklog jiraWorklog, Worklog worklog, String jiraBaseUrl)
    {
        return new Worklog(worklog.issueId(),
            jiraWorklog.id(),
            worklog.issueKey(),
            worklog.issueSummary(),
            jiraWorklog.started(),
            jiraWorklog.started()
                .plusSeconds(jiraWorklog.timeSpentSeconds()),
            jiraWorklog.comment(),
            jiraBaseUrl);
    }
}
