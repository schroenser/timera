package de.schroenser.timera;

import java.time.OffsetDateTime;
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

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.jira.bc.issue.worklog.WorklogInputParameters;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.IssueManager;
import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.jql.parser.JqlParseException;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;

@Path("/worklog")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorklogController
{
    @ComponentImport
    private final IssueManager issueManager;

    private final WorklogListService listService;
    private final WorklogCreateService createService;
    private final WorklogUpdateService updateService;
    private final WorklogDeleteService deleteService;
    private final WorklogMapper mapper;

    @GET
    public Response list(@QueryParam("start") String start, @QueryParam("end") String end)
        throws JqlParseException, SearchException
    {
        return Response.ok(listService.list(OffsetDateTime.parse(start), OffsetDateTime.parse(end))
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()))
            .build();
    }

    @POST
    public Response create(WorklogDto dto)
    {
        Issue issue = issueManager.getIssueObject(dto.getIssueId());
        WorklogInputParameters inputParameters = mapper.toInputParameters(dto, issue);
        return Response.ok(mapper.toDto(createService.create(inputParameters)))
            .build();
    }

    @PUT
    @Path("{id}")
    public Response update(@PathParam("id") long id, WorklogDto dto)
    {
        if (id != dto.getWorklogId())
        {
            return Response.status(Response.Status.BAD_REQUEST)
                .build();
        }

        Issue issue = issueManager.getIssueObject(dto.getIssueId());
        WorklogInputParameters inputParameters = mapper.toInputParameters(dto, issue);
        return Response.ok(mapper.toDto(updateService.update(inputParameters)))
            .build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") long id)
    {
        boolean success = deleteService.delete(id);

        if (success)
        {
            return Response.ok()
                .build();
        }

        return Response.serverError()
            .build();
    }
}
