package de.schroenser.timera.jira;

import java.util.List;

import de.schroenser.timera.jira.paged.PagedResponse;

public record IssueResponse(int startAt, int maxResults, int total, List<JiraIssue> issues)
    implements PagedResponse<JiraIssue>
{
    @Override
    public List<JiraIssue> elements()
    {
        return issues();
    }
}
