package de.schroenser.timera;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import com.atlassian.query.Query;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IssuePickerService
{
    private static final Pattern ISSUE_KEY_PATTERN = Pattern.compile("[A-Za-z]{2,}-\\d+");

    @ComponentImport
    private final JiraAuthenticationContext jiraAuthenticationContext;

    @ComponentImport
    private final JqlQueryParser jqlQueryParser;

    @ComponentImport
    private final SearchService searchService;

    public List<Issue> search(String query) throws JqlParseException, SearchException
    {
        String escapedQuery = query.replace("\"", "\\\"");

        String jql = null;

        if (ISSUE_KEY_PATTERN.matcher(escapedQuery)
            .matches())
        {
            jql = "key = " + escapedQuery;
        }
        else if (!escapedQuery.isEmpty())
        {
            jql = "text ~ \"" + escapedQuery + "\"";
        }

        if (jql == null)
        {
            return Collections.emptyList();
        }

        ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();

        Query parsedQuery = jqlQueryParser.parseQuery(jql);

        return searchService.search(loggedInUser, parsedQuery, PagerFilter.newPageAlignedFilter(0, 19))
            .getResults();
    }
}
