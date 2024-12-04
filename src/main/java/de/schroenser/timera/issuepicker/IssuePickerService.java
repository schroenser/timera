package de.schroenser.timera.issuepicker;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.issue.JiraIssueService;
import de.schroenser.timera.jira.issuepicker.JiraIssuePickerService;

@Service
@RequiredArgsConstructor
public class IssuePickerService
{
    private final JiraIssuePickerService jiraIssuePickerService;
    private final JiraIssueService jiraIssueService;

    public List<Issue> search(String query)
    {
        var issuePickerKeys = jiraIssuePickerService.searchIssuePickerKeys(query);

        var jiraIssues = jiraIssueService.getIssuesForIssuePicker(issuePickerKeys, query);

        return jiraIssues.stream()
            .map(jiraIssue -> new Issue(jiraIssue.id(),
                jiraIssue.key(),
                jiraIssue.fields()
                    .summary()))
            .toList();
    }
}
