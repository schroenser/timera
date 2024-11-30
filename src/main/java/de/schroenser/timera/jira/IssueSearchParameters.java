package de.schroenser.timera.jira;

import java.util.List;

public record IssueSearchParameters(String jql, List<String> fields, int startAt, int maxResults)
{
    public IssueSearchParameters(int startAt)
    {
        this("updatedDate >= startOfWeek('+1d') and timespent > 0 order by updatedDate asc",
            List.of("summary"),
            startAt,
            1000);
    }
}
