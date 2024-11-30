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
            worklog.started(),
            worklog.timeSpentSeconds(),
            worklog.worklogComment());
    }
}
