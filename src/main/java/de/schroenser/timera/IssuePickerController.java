package de.schroenser.timera;

import java.util.stream.Collectors;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;

import com.atlassian.jira.issue.search.SearchException;
import com.atlassian.jira.jql.parser.JqlParseException;

@Path("/issuepicker")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IssuePickerController
{
    private final IssuePickerService service;
    private final IssueMapper mapper;

    @GET
    public Response search(@QueryParam("query") String query) throws JqlParseException, SearchException
    {
        return Response.ok(service.search(query)
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList()))
            .build();
    }
}
