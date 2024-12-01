import {useCallback, useEffect, useState} from "react";
import "react-big-calendar/lib/css/react-big-calendar.css";
import {Calendar, Event, momentLocalizer} from "react-big-calendar";
import moment from "moment";
import "moment/locale/de";
import {Worklog} from "./worklog";
import "./App.css";
import {eventColors} from "./eventColors";

function App() {
    moment.locale("de");
    const localizer = momentLocalizer(moment);

    const max = new Date(1970, 0, 1, 7, 0, 0);

    let [start, setStart] = useState<moment.Moment>(moment().startOf("week"));
    let [end, setEnd] = useState<moment.Moment>(moment().endOf("week"));
    let [events, setEvents] = useState<Event[]>([]);

    useEffect(() => {
        fetch("/api/worklog?" + new URLSearchParams({
            start: start.toISOString(),
            end: end.toISOString()
        }))
        .then(response => response.json())
        .then(json => json as Worklog[])
        .then(worklogs => worklogs.map(worklog => {
            return {
                start: moment(worklog.start).toDate(),
                end: moment(worklog.end).toDate(),
                title: worklog.issueKey + ": " + worklog.issueSummary
            };
        }))
        .then(events => setEvents(events));
    }, [start, end, setEvents]);

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

    const onNavigate = useCallback((newDate: Date) => {
        setStart(moment(newDate).startOf("week"));
        setEnd(moment(newDate).endOf("week"));
    }, [setStart, setEnd]);

    return (
        <Calendar localizer={localizer} views={["week"]} view="week" scrollToTime={max} step={15} timeslots={4} dayLayoutAlgorithm={"no-overlap"} events={events} eventPropGetter={eventPropGetter} onNavigate={onNavigate}/>
    );
}

export default App;
