package de.schroenser.timera.jira.worklog;

import java.util.List;

record WorklogResponse(List<JiraWorklog> worklogs)
{
}
