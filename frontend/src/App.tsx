import {useCallback, useEffect, useMemo, useState} from "react";
import {Calendar, DayLayoutAlgorithm, Event, momentLocalizer, Views} from "react-big-calendar";
import "react-big-calendar/lib/css/react-big-calendar.css";
import withDragAndDrop, {EventInteractionArgs} from "react-big-calendar/lib/addons/dragAndDrop";
import "react-big-calendar/lib/addons/dragAndDrop/styles.css";
import moment from "moment";
import "moment/locale/de";
import {Worklog} from "./worklog";
import "./App.css";
import {eventColors} from "./eventColors";

async function getWorklogs(start: moment.Moment, end: moment.Moment): Promise<Worklog[]> {
    const response = await fetch("/api/worklog?" + new URLSearchParams({
        start: start.toISOString(),
        end: end.toISOString()
    }), {
        headers: new Headers({
            "Accept": "application/json; charset=UTF-8"
        })
    });
    return await response.json() as Worklog[];
}

async function updateWorklog(worklog: Worklog): Promise<Worklog> {
    const response = await fetch(`/api/worklog/${worklog.worklogId}`, {
        method: "PUT",
        body: JSON.stringify(worklog),
        headers: new Headers({
            "Content-Type": "application/json; charset=UTF-8",
            "Accept": "application/json; charset=UTF-8"
        })
    });
    return await response.json() as Worklog;
}

function createEvent(worklog: Worklog): Event {
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

function App() {
    const DragAndDropCalendar = withDragAndDrop(Calendar);

    moment.locale("de");
    const localizer = momentLocalizer(moment);

    const {
        views,
        defaultView,
        scrollToTime,
        step,
        timeslots,
        dayLayoutAlgorithm
    } = useMemo(() => (
        {
            views: {
                work_week: true
            },
            defaultView: Views.WORK_WEEK,
            scrollToTime: new Date(1970, 0, 1, 7, 0, 0),
            step: 15,
            timeslots: 4,
            dayLayoutAlgorithm: "no-overlap" as DayLayoutAlgorithm
        }
    ), []);

    const [events, setEvents] = useState<Event[]>([]);

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

    const [date, setDate] = useState<Date>(new Date());

    useEffect(() => {
        getWorklogs(moment(date).startOf("week"), moment(date).endOf("week"))
        .then(worklogs => worklogs.map(createEvent))
        .then(setEvents);
    }, [date, setEvents]);

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
        updateWorklog(modifiedWorklog)
        .then(createEvent)
        .then(event => {
            setEvents((prev) => {
                const filtered = prev.filter((ev) => ev.resource.worklogId !== event.resource.worklogId);
                return [
                    ...filtered, event
                ];
            });
        });
    }, [setEvents]);

    return (
        <DragAndDropCalendar localizer={localizer}
            views={views}
            defaultView={defaultView}
            scrollToTime={scrollToTime}
            step={step}
            timeslots={timeslots}
            dayLayoutAlgorithm={dayLayoutAlgorithm}
            events={events}
            eventPropGetter={eventPropGetter}
            date={date}
            onNavigate={setDate}
            onEventDrop={onEventChange}
            onEventResize={onEventChange}/>
    );
}

export default App;
