package de.schroenser.timera.jira.issue;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

record IssueSearchParameters(String jql, List<String> fields, int startAt, int maxResults)
{
    private static final DateTimeFormatter JQL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public IssueSearchParameters(OffsetDateTime start, int startAt)
    {
        this("updatedDate >= '" +
            JQL_DATE_TIME_FORMATTER.format(start) +
            "' and timespent > 0 order by updatedDate asc", List.of("summary", "updated"), startAt, 1000);
    }
}
