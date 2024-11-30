package de.schroenser.timera.jira;

import java.time.OffsetDateTime;

public record JiraWorklog(String id, OffsetDateTime started, int timeSpentSeconds, String comment, JiraAuthor author,
                          JiraAuthor updateAuthor)
{
}
