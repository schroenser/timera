package de.schroenser.timera.jira.issue;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import de.schroenser.timera.jira.paged.PagedResponseSpliterator;

@Slf4j
@Service
@RequiredArgsConstructor
public class JiraIssueService
{
    private static final DateTimeFormatter JQL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final Pattern ISSUE_KEY_PATTERN = Pattern.compile("[A-Z]{2,}-\\d+");

    private final RestClient restClient;

    public List<JiraIssue> getUpdatedIssuesWithWorklogs(OffsetDateTime start)
    {
        var jql = "updatedDate >= '" +
            JQL_DATE_TIME_FORMATTER.format(start) +
            "' and timespent > 0 order by updatedDate asc";

        return performSearch(jql);
    }

    public List<JiraIssue> getIssuesForIssuePicker(List<String> issueKeys, String query)
    {
        if (ISSUE_KEY_PATTERN.matcher(query)
            .matches())
        {
            issueKeys = Stream.concat(issueKeys.stream(), Stream.of(query))
                .toList();
        }

        var jql = "";

        if (!issueKeys.isEmpty())
        {
            jql = jql + "key in (" + String.join(",", issueKeys) + ")";
        }

        if (!issueKeys.isEmpty() && !query.isEmpty())
        {
            jql = jql + " or ";
        }

        if (!query.isEmpty())
        {
            jql = jql + "text ~ '" + query + "'";
        }

        log.info("JQL: {}", jql);

        var issueSearchParameters = new IssueSearchParameters(jql, 0, 19);

        return performPageSearch(issueSearchParameters).issues();
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
        return restClient.post()
            .uri("rest/api/2/search")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .body(issueSearchParameters)
            .retrieve()
            .body(IssueResponse.class);
    }
}
