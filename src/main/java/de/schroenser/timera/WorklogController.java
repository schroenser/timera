package de.schroenser.timera;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

import com.atlassian.jira.bc.JiraServiceContext;
import com.atlassian.jira.bc.JiraServiceContextImpl;
import com.atlassian.jira.bc.issue.search.SearchService;
import com.atlassian.jira.bc.issue.worklog.WorklogInputParameters;
import com.atlassian.jira.bc.issue.worklog.WorklogResult;
import com.atlassian.jira.bc.issue.worklog.WorklogService;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.jira.jql.parser.JqlQueryParser;
import com.atlassian.jira.security.JiraAuthenticationContext;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.web.bean.PagerFilter;
import com.atlassian.query.Query;

@Slf4j
@Path("/worklog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorklogController
{
    @FunctionalInterface
    private interface ValidateFunction
    {
        WorklogResult apply(JiraServiceContext jiraServiceContext, WorklogInputParameters worklogInputParameters);
    }

    @FunctionalInterface
    private interface CreateOrUpdateFunction
    {
        com.atlassian.jira.issue.worklog.Worklog apply(
            JiraServiceContext jiraServiceContext, WorklogResult worklogResult, boolean dispatchEvent);
    }

    private static final DateTimeFormatter JQL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final JiraAuthenticationContext
        jiraAuthenticationContext
        = ComponentAccessor.getJiraAuthenticationContext();
    private final JqlQueryParser
        jqlQueryParser
        = ComponentAccessor.getOSGiComponentInstanceOfType(JqlQueryParser.class);
    private final SearchService searchService = ComponentAccessor.getOSGiComponentInstanceOfType(SearchService.class);
    private final IssueManager issueManager = ComponentAccessor.getIssueManager();
    private final WorklogService
        worklogService
        = ComponentAccessor.getOSGiComponentInstanceOfType(WorklogService.class);

    @GET
    public Response list(@QueryParam("start") String start, @QueryParam("end") String end)
        throws JqlParseException, SearchException
    {
        String jql = "updatedDate >= '" +
            OffsetDateTime.parse(start)
                .format(JQL_DATE_TIME_FORMATTER) +
            "' and timespent > 0 order by updatedDate asc";

        ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();

        Query parsedQuery = jqlQueryParser.parseQuery(jql);

        JiraServiceContextImpl jiraServiceContext = new JiraServiceContextImpl(loggedInUser);

        List<Worklog> worklogs = searchService.search(loggedInUser, parsedQuery, PagerFilter.getUnlimitedFilter())
            .getResults()
            .stream()
            .flatMap(jiraIssue -> worklogService.getByIssueVisibleToUser(jiraServiceContext, jiraIssue)
                .stream())
            .filter(worklog1 -> worklog1.getAuthorObject()
                .equals(loggedInUser))
            .filter(worklog -> worklog.getStartDate()
                .toInstant()
                .atOffset(ZoneOffset.UTC)
                .plusSeconds(worklog.getTimeSpent())
                .isAfter(OffsetDateTime.parse(start)) &&
                worklog.getStartDate()
                    .toInstant()
                    .atOffset(ZoneOffset.UTC)
                    .isBefore(OffsetDateTime.parse(end)))
            .map(WorklogMapper::fromJiraWorklog)
            .collect(Collectors.toList());

        return Response.ok(worklogs)
            .build();
    }

    @POST
    public Response create(Worklog worklog)
    {
        return apply(worklog, worklogService::validateCreate, worklogService::createAndRetainRemainingEstimate);
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") long id, Worklog worklog)
    {
        if (id != worklog.getWorklogId())
        {
            return Response.status(Response.Status.BAD_REQUEST)
                .build();
        }

        return apply(worklog, worklogService::validateUpdate, worklogService::updateAndRetainRemainingEstimate);
    }

    private Response apply(
        Worklog worklog, ValidateFunction validate, CreateOrUpdateFunction execute)
    {
        ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();
        com.atlassian.jira.issue.Issue issue = issueManager.getIssueObject(worklog.getIssueId());
        WorklogInputParameters worklogInputParameters = WorklogMapper.toWorklogInputParameters(issue, worklog);
        JiraServiceContextImpl jiraServiceContext = new JiraServiceContextImpl(loggedInUser);
        WorklogResult worklogResult = validate.apply(jiraServiceContext, worklogInputParameters);
        com.atlassian.jira.issue.worklog.Worklog jiraWorklog = execute.apply(jiraServiceContext, worklogResult, true);
        Worklog appliedWorklog = WorklogMapper.fromJiraWorklog(jiraWorklog);
        return Response.ok(appliedWorklog)
            .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id, @QueryParam("issueId") String issueId)
    {
        ApplicationUser loggedInUser = jiraAuthenticationContext.getLoggedInUser();
        JiraServiceContextImpl jiraServiceContext = new JiraServiceContextImpl(loggedInUser);
        WorklogResult worklogResult = worklogService.validateDelete(jiraServiceContext, id);
        boolean success = worklogService.deleteAndRetainRemainingEstimate(jiraServiceContext, worklogResult, true);

        if (success)
        {
            return Response.ok()
                .build();
        }

        return Response.serverError()
            .build();
    }
}
