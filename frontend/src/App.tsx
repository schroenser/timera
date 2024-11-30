import "./App.css";
import "react-big-calendar/lib/css/react-big-calendar.css";
import {Calendar, momentLocalizer} from "react-big-calendar";
import moment from "moment";

function App() {
    const localizer = momentLocalizer(moment);

    return (
        <Calendar localizer={localizer}/>
    );
}

export default App;
