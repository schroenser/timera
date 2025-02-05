package de.schroenser.timera.issuepicker;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.issue.JiraIssueService;

@Service
@RequiredArgsConstructor
public class IssuePickerService
{
    private final JiraIssueService jiraIssueService;

    public List<Issue> search(String query)
    {
        var jiraIssues = jiraIssueService.getIssuesForIssuePicker(query);

        return jiraIssues.stream()
            .map(jiraIssue -> new Issue(jiraIssue.id(),
                jiraIssue.key(),
                jiraIssue.fields()
                    .summary()))
            .toList();
    }
}
