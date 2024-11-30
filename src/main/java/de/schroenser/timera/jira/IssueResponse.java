package de.schroenser.timera.jira;

import java.util.List;

public record IssueResponse(int startAt, int maxResults, int total, List<JiraIssue> issues)
    implements PagedResponse<JiraIssue>
{
    @Override
    public List<JiraIssue> elements()
    {
        return issues();
    }
}
