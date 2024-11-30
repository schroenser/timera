package de.schroenser.timera.jira;

import java.time.OffsetDateTime;

public record JiraFields(String summary, OffsetDateTime updated)
{
}
