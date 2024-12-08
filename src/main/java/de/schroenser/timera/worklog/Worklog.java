package de.schroenser.timera.worklog;

import java.time.OffsetDateTime;

public record Worklog(String issueId, String worklogId, String issueKey, String issueSummary, OffsetDateTime start,
                      OffsetDateTime end, String worklogComment, String issueUrl, String worklogUrl)
{
    public Worklog(
        String issueId,
        String worklogId,
        String issueKey,
        String issueSummary,
        OffsetDateTime start,
        OffsetDateTime end,
        String worklogComment,
        String jiraBaseUrl)
    {
        this(issueId,
            worklogId,
            issueKey,
            issueSummary,
            start,
            end,
            worklogComment,
            jiraBaseUrl + "/browse/" + issueKey,
            jiraBaseUrl +
                "/browse/" +
                issueKey +
                "?focusedWorklogId=" +
                worklogId +
                "&page=com.atlassian.jira.plugin.system.issuetabpanels:worklog-tabpanel");
    }
}
