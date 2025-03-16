package de.schroenser.timera;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.issue.worklog.WorklogService;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.issue.worklog.Worklog;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.query.Query;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorklogListService
{
    private static final DateTimeFormatter JQL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;

    @ComponentImport
    private final JqlQueryParser jqlQueryParser;

    @ComponentImport
    private final SearchService searchService;

    @ComponentImport
    private final WorklogService worklogService;

    public List<Worklog> list(OffsetDateTime start, OffsetDateTime end) throws JqlParseException, SearchException
    {
        String jql = "updatedDate >= '" +
            start.format(JQL_DATE_TIME_FORMATTER) +
            "' and timespent > 0 order by updatedDate asc";

        ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();

        Query parsedQuery = jqlQueryParser.parseQuery(jql);

        JiraServiceContext jiraServiceContext = new JiraServiceContextImpl(loggedInUser);

        return searchService.search(loggedInUser, parsedQuery, PagerFilter.getUnlimitedFilter())
            .getResults()
            .stream()
            .flatMap(jiraIssue -> worklogService.getByIssueVisibleToUser(jiraServiceContext, jiraIssue)
                .stream())
            .filter(worklog -> worklog.getAuthorObject()
                .equals(loggedInUser))
            .filter(worklog -> worklog.getStartDate()
                .toInstant()
                .atOffset(ZoneOffset.UTC)
                .plusSeconds(worklog.getTimeSpent())
                .isAfter(start) &&
                worklog.getStartDate()
                    .toInstant()
                    .atOffset(ZoneOffset.UTC)
                    .isBefore(end))
            .collect(Collectors.toList());
    }
}
