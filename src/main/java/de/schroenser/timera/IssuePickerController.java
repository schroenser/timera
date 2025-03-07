package de.schroenser.timera;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.jql.builder.JqlQueryBuilder;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;

@Path("/issuepicker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class IssuePickerController
{
    private static final Pattern ISSUE_KEY_PATTERN = Pattern.compile("[A-Za-z]{2,}-\\d+");

    private final JiraAuthenticationContext
        jiraAuthenticationContext
        = ComponentAccessor.getJiraAuthenticationContext();
    private final JqlQueryParser
        jqlQueryParser
        = ComponentAccessor.getOSGiComponentInstanceOfType(JqlQueryParser.class);
    private final SearchService searchService = ComponentAccessor.getOSGiComponentInstanceOfType(SearchService.class);

    @GET
    public Response search(@QueryParam("query") String query) throws JqlParseException, SearchException
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

        ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();

        Query parsedQuery = jqlQueryParser.parseQuery(jql);

        List<Issue> issuePickerIssues = searchService.search(loggedInUser,
                parsedQuery,
                PagerFilter.newPageAlignedFilter(0, 19))
            .getResults()
            .stream()
            .map(IssueMapper::fromJiraIssue)
            .collect(Collectors.toList());

        return Response.ok(issuePickerIssues)
            .build();
    }
}
