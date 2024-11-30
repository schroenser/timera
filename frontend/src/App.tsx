import "./App.css";
import {useCallback, useEffect, useState} from "react";
import "react-big-calendar/lib/css/react-big-calendar.css";
import {Calendar, Event, momentLocalizer} from "react-big-calendar";
import moment from "moment";
import "moment/locale/de";
import {Worklog} from "./worklog";

function App() {
    moment.locale("de");
    const localizer = momentLocalizer(moment);

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
                title: worklog.issueSummary
            };
        }))
        .then(events => setEvents(events));
    }, [start, end, setEvents]);

    const onNavigate = useCallback((newDate: Date) => {
        setStart(moment(newDate).startOf("week"));
        setEnd(moment(newDate).endOf("week"));
    }, [setStart, setEnd]);

    return (
        <Calendar localizer={localizer} view="week" views={["week"]} events={events} onNavigate={onNavigate}/>
    );
}

export default App;
