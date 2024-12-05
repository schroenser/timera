package de.schroenser.timera.jira.issue;

import java.util.List;

import de.schroenser.timera.jira.paged.PagedResponse;

record IssueResponse(int startAt, int maxResults, int total, List<JiraIssue> issues) implements PagedResponse<JiraIssue>
{
    @Override
    public List<JiraIssue> elements()
    {
        return issues();
    }
}
