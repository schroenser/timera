import {useCallback, useEffect, useMemo, useState} from "react";
import {Calendar, DayLayoutAlgorithm, Event, momentLocalizer, Views} from "react-big-calendar";
import "react-big-calendar/lib/css/react-big-calendar.css";
import moment from "moment";
import "moment/locale/de";
import {Worklog} from "./worklog";
import "./App.css";
import {eventColors} from "./eventColors";

async function getEvents(start: moment.Moment, end: moment.Moment): Promise<Event[]> {
    const response = await fetch("/api/worklog?" + new URLSearchParams({
        start: start.toISOString(),
        end: end.toISOString()
    }), {
        headers: new Headers({
            "Accept": "application/json; charset=UTF-8"
        })
    });
    const worklogs = await response.json() as Worklog[];
    return worklogs.map(worklog => {
        return {
            start: moment(worklog.start).toDate(),
            end: moment(worklog.end).toDate(),
            title: worklog.issueKey + ": " + worklog.issueSummary,
            resource: worklog
        };
    });
}

function App() {
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
                week: true
            },
            defaultView: Views.WEEK,
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
        getEvents(moment(date).startOf("week"), moment(date).endOf("week"))
        .then(setEvents);
    }, [date, setEvents]);

    return (
        <Calendar localizer={localizer}
            views={views}
            defaultView={defaultView}
            scrollToTime={scrollToTime}
            step={step}
            timeslots={timeslots}
            dayLayoutAlgorithm={dayLayoutAlgorithm}
            events={events}
            eventPropGetter={eventPropGetter}
            date={date}
            onNavigate={setDate}/>
    );
}

export default App;
