package de.schroenser.timera.jira.issue;

import java.time.OffsetDateTime;

public record JiraFields(String summary, OffsetDateTime updated)
{
}
