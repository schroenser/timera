package de.schroenser.timera.jira.issue;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import de.schroenser.timera.jira.paged.PagedResponseSpliterator;

@Service
@RequiredArgsConstructor
public class JiraIssueService
{
    private static final DateTimeFormatter JQL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Pattern ISSUE_KEY_PATTERN = Pattern.compile("[A-Za-z]{2,}-\\d+");

    private final RestClient restClient;

    public List<JiraIssue> getUpdatedIssuesWithWorklogs(OffsetDateTime start)
    {
        var jql = "updatedDate >= '" +
            JQL_DATE_TIME_FORMATTER.format(start) +
            "' and timespent > 0 order by updatedDate asc";

        return performSearch(jql);
    }

    public List<JiraIssue> getIssuesForIssuePicker(String query)
    {
        var escapedQuery = query.replace("\"", "\\\"");

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

        List<JiraIssue> issues = List.of();

        if (jql != null)
        {
            var issueSearchParameters = new IssueSearchParameters(jql, 0, 19);
            issues = performPageSearch(issueSearchParameters).issues();
        }

        return issues;
    }

    private List<JiraIssue> performSearch(String jql)
    {
        Function<Integer, IssueResponse> pageProvider = startAt -> performPageSearch(new IssueSearchParameters(jql,
            startAt));
        return StreamSupport.stream(new PagedResponseSpliterator<>(pageProvider), false)
            .toList();
    }

    private IssueResponse performPageSearch(IssueSearchParameters issueSearchParameters)
    {
        RestClient.ResponseSpec retrieve = restClient.post()
            .uri("rest/api/2/search")
            .body(issueSearchParameters)
            .retrieve();
        IssueResponse body;
        try
        {
            body = retrieve.body(IssueResponse.class);
        }
        catch (HttpClientErrorException.BadRequest e)
        {
            if (e.getMessage()
                .contains("400 Bad Request: \"{\"errorMessages\":[\"An issue with key '"))
            {
                body = new IssueResponse(0, 0, 0, List.of());
            }
            else
            {
                throw e;
            }
        }
        return body;
    }
}
