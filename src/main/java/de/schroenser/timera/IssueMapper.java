package de.schroenser.timera;

import org.springframework.stereotype.Component;

import com.atlassian.jira.issue.Issue;

@Component
class IssueMapper
{
    public IssueDto toDto(Issue pojo)
    {
        return new IssueDto(pojo.getId(), pojo.getKey(), pojo.getSummary());
    }
}
