package de.schroenser.timera.jira.issue;

import java.util.List;

record IssueSearchParameters(String jql, List<String> fields, int startAt, int maxResults)
{
    public IssueSearchParameters(String jql, int startAt, int maxResults)
    {
        this(jql, List.of("summary", "updated"), startAt, maxResults);
    }

    public IssueSearchParameters(String jql, int startAt)
    {
        this(jql, startAt, 1000);
    }
}
