package de.schroenser.timera.issuepicker;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import de.schroenser.timera.jira.issuepicker.JiraIssuePickerService;

@Service
@RequiredArgsConstructor
public class IssuePickerService
{
    private final JiraIssuePickerService service;

    public List<IssuePickerIssue> search(String query)
    {
        return service.searchIssuePickerItems(query)
            .stream()
            .map(jiraIssuePickerIssue -> new IssuePickerIssue(jiraIssuePickerIssue.key(),
                jiraIssuePickerIssue.summaryText()))
            .toList();
    }
}
