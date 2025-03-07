package de.schroenser.timera;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import com.atlassian.jira.bc.issue.worklog.WorklogInputParameters;
import com.atlassian.jira.bc.issue.worklog.WorklogInputParametersImpl;

@Slf4j
@UtilityClass
class WorklogMapper
{
    public WorklogInputParameters toWorklogInputParameters(com.atlassian.jira.issue.Issue issue, Worklog worklog)
    {
        OffsetDateTime start = OffsetDateTime.parse(worklog.getStart());
        OffsetDateTime end = OffsetDateTime.parse(worklog.getEnd());
        long timeSpent = start.until(end, ChronoUnit.MINUTES);
        Date startDate = Date.from(start.toInstant());
        String timeSpentString = String.valueOf(timeSpent);
        return WorklogInputParametersImpl.builder()
            .issue(issue)
            .worklogId(worklog.getWorklogId())
            .startDate(startDate)
            .timeSpent(timeSpentString)
            .comment(worklog.getWorklogComment())
            .build();
    }

    public Worklog fromJiraWorklog(com.atlassian.jira.issue.worklog.Worklog jiraWorklog)
    {
        Date startDate = jiraWorklog.getStartDate();
        long timeSpent = jiraWorklog.getTimeSpent();
        OffsetDateTime start = startDate.toInstant()
            .atOffset(ZoneOffset.UTC);
        OffsetDateTime end = start.plusSeconds(timeSpent);
        String startString = start.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String endString = end.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        return new Worklog(jiraWorklog.getIssue()
            .getId(),
            jiraWorklog.getId(),
            jiraWorklog.getIssue()
                .getKey(),
            jiraWorklog.getIssue()
                .getSummary(),
            startString,
            endString,
            jiraWorklog.getComment());
    }
}
