import "./App.css";
import {useEffect, useState} from "react";
import "react-big-calendar/lib/css/react-big-calendar.css";
import {Calendar, Event, momentLocalizer} from "react-big-calendar";
import moment from "moment";
import {Worklog} from "./worklog";

function App() {
    const localizer = momentLocalizer(moment);

    let [events, setEvents] = useState<Event[]>([]);

    useEffect(() => {
        fetch("/api/worklog")
        .then(response => response.json())
        .then(json => json as Worklog[])
        .then(worklogs => worklogs.map(worklog => {
            return {
                start: moment(worklog.started).toDate(),
                end: moment(worklog.started).add(worklog.timeSpentSeconds, "seconds").toDate(),
                title: worklog.issueSummary
            };
        }))
        .then(events => setEvents(events));
    }, [setEvents]);

    return (
        <Calendar localizer={localizer} view="week" views={["week"]} events={events}/>
    );
}

export default App;
