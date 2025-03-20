package de.schroenser.timera;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.atlassian.jira.bc.issue.worklog.WorklogInputParameters;
import com.atlassian.jira.bc.issue.worklog.WorklogInputParametersImpl;
import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.worklog.Worklog;

@Component
class WorklogMapper
{
    public WorklogInputParameters toInputParameters(WorklogDto dto, Issue issue)
    {
        OffsetDateTime start = OffsetDateTime.parse(dto.getStart());
        OffsetDateTime end = OffsetDateTime.parse(dto.getEnd());
        long timeSpent = start.until(end, ChronoUnit.MINUTES);
        Date startDate = Date.from(start.toInstant());
        String timeSpentString = timeSpent + "m";
        return WorklogInputParametersImpl.builder()
            .issue(issue)
            .worklogId(dto.getWorklogId())
            .startDate(startDate)
            .timeSpent(timeSpentString)
            .comment(dto.getWorklogComment())
            .build();
    }

    public WorklogDto toDto(Worklog pojo)
    {
        Date startDate = pojo.getStartDate();
        long timeSpent = pojo.getTimeSpent();
        OffsetDateTime start = startDate.toInstant()
            .atOffset(ZoneOffset.UTC);
        OffsetDateTime end = start.plusSeconds(timeSpent);
        return new WorklogDto(pojo.getIssue()
            .getId(),
            pojo.getId(),
            pojo.getIssue()
                .getKey(),
            pojo.getIssue()
                .getSummary(),
            start.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            end.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            pojo.getComment());
    }
}
