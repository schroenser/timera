package de.schroenser.timera.worklog;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WorklogDtoMapper
{
    public WorklogDto fromPojo(Worklog worklog)
    {
        if (worklog == null)
        {
            return null;
        }

        return new WorklogDto(worklog.issueId(),
            worklog.worklogId(),
            worklog.issueKey(),
            worklog.issueSummary(),
            worklog.start(),
            worklog.end(),
            worklog.worklogComment());
    }

    public Worklog toPojo(WorklogDto worklogDto)
    {
        if (worklogDto == null)
        {
            return null;
        }

        return new Worklog(worklogDto.issueId(),
            worklogDto.worklogId(),
            worklogDto.issueKey(),
            worklogDto.issueSummary(),
            worklogDto.start(),
            worklogDto.end(),
            worklogDto.worklogComment());
    }
}
