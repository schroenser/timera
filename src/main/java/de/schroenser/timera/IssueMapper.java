package de.schroenser.timera;

import lombok.experimental.UtilityClass;

@UtilityClass
class IssueMapper
{
    public Issue fromJiraIssue(com.atlassian.jira.issue.Issue jiraIssue)
    {
        return new Issue(jiraIssue.getId(), jiraIssue.getKey(), jiraIssue.getSummary());
    }
}
