package de.schroenser.timera.jira;

import java.util.List;

public record WorklogResponse(List<JiraWorklog> worklogs)
{
}
