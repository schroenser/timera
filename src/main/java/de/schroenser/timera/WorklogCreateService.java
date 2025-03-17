package de.schroenser.timera;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.issue.worklog.WorklogInputParameters;
import com.atlassian.jira.bc.issue.worklog.WorklogResult;
import com.atlassian.jira.bc.issue.worklog.WorklogService;
import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorklogCreateService
{
    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;

    @ComponentImport
    private final WorklogService worklogService;

    public Worklog create(WorklogInputParameters inputParameters)
    {
        ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();
        JiraServiceContext jiraServiceContext = new JiraServiceContextImpl(loggedInUser);
        WorklogResult worklogResult = worklogService.validateCreate(jiraServiceContext, inputParameters);
        return worklogService.createAndRetainRemainingEstimate(jiraServiceContext, worklogResult, true);
    }
}
