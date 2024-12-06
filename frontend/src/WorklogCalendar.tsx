import React, {useCallback, useMemo, useState} from "react";
import moment from "moment/moment";
import "moment/locale/de";
import {Calendar, DayLayoutAlgorithm, Event, momentLocalizer, Views} from "react-big-calendar";
import withDragAndDrop, {EventInteractionArgs} from "react-big-calendar/lib/addons/dragAndDrop";
import Worklog from "./Worklog";
import eventColors from "./eventColors";
import "react-big-calendar/lib/css/react-big-calendar.css";
import "react-big-calendar/lib/addons/dragAndDrop/styles.css";

function toEvent(worklog: Worklog): Event {
    let title = "";
    if (worklog.worklogComment) {
        title = worklog.worklogComment + " (";
    }
    title = title + worklog.issueKey + ": " + worklog.issueSummary;
    if (worklog.worklogComment) {
        title = title + ")";
    }
    return {
        start: moment(worklog.start).toDate(),
        end: moment(worklog.end).toDate(),
        title: title,
        resource: worklog
    };
}

type WorklogCalendarProps = {
    worklogs: Worklog[], onNavigate: (date: moment.Moment) => void, onWorklogChange: (worklog: Worklog) => void
}

function WorklogCalendar({
    worklogs,
    onNavigate,
    onWorklogChange
}: WorklogCalendarProps) {
    const {
        defaultView,
        views,
        step,
        timeslots,
        scrollToTime,
        dayLayoutAlgorithm
    } = useMemo(() => (
        {
            defaultView: Views.WORK_WEEK,
            views: {
                work_week: true
            },
            step: 15,
            timeslots: 4,
            scrollToTime: new Date(1970, 0, 1, 7, 0, 0),
            dayLayoutAlgorithm: "no-overlap" as DayLayoutAlgorithm
        }
    ), []);

    const DragAndDropCalendar = withDragAndDrop(Calendar);

    moment.locale("de");
    const localizer = momentLocalizer(moment);

    const [date, setDate] = useState<Date>(new Date());

    const onNavigateInternal = useCallback((date: Date) => {
        setDate(date);
        onNavigate(moment(date));
    }, [setDate, onNavigate]);

    const eventPropGetter = useCallback((event: Event) => {
        const {
            color,
            backgroundColor
        } = eventColors(event);
        return {
            style: {
                color,
                backgroundColor
            }
        };
    }, []);

    const onEventChange = useCallback(({
        event,
        start,
        end
    }: EventInteractionArgs<Event>) => {
        const worklog = event.resource as Worklog;
        const modifiedWorklog = {
            ...worklog,
            start: start,
            end: end
        } as Worklog;
        onWorklogChange(modifiedWorklog);
    }, [onWorklogChange]);

    return (
        <DragAndDropCalendar localizer={localizer}
            date={date}
            onNavigate={onNavigateInternal}
            defaultView={defaultView}
            events={worklogs.map(toEvent)}
            views={views}
            step={step}
            timeslots={timeslots}
            eventPropGetter={eventPropGetter}
            scrollToTime={scrollToTime}
            dayLayoutAlgorithm={dayLayoutAlgorithm}
            onEventDrop={onEventChange}
            onEventResize={onEventChange}/>
    );
}

export default WorklogCalendar;
