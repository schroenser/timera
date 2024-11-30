package de.schroenser.timera.worklog;

import java.time.OffsetDateTime;

public record Worklog(String issueId, String worklogId, String issueKey, String issueSummary, OffsetDateTime start,
                      OffsetDateTime end, String worklogComment)
{
}
