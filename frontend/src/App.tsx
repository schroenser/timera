import {useCallback, useEffect, useState} from "react";
import moment from "moment";
import Worklog from "./Worklog";
import WorklogCalendar from "./WorklogCalendar";
import "./App.css";
import {useDisclosure} from "@mantine/hooks";
import {SlotInfo} from "react-big-calendar";
import CreateDialog from "./CreateDialog";

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

async function createWorklog(worklog: Worklog): Promise<Worklog> {
    const response = await fetch(`/api/worklog`, {
        method: "POST",
        body: JSON.stringify(worklog),
        headers: new Headers({
            "Content-Type": "application/json; charset=UTF-8",
            "Accept": "application/json; charset=UTF-8"
        })
    });
    return await response.json() as Worklog;
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

function App() {
    const [worklogs, setWorklogs] = useState<Worklog[]>([]);

    const onNavigate = useCallback((date: moment.Moment) => {
        getWorklogs(moment(date).startOf("week"), moment(date).endOf("week"))
        .then(setWorklogs);
    }, [setWorklogs]);

    useEffect(() => {
        onNavigate(moment());
    }, [onNavigate]);

    const onWorklogChange = useCallback((worklog: Worklog) => {
        updateWorklog(worklog)
        .then(worklog => {
            setWorklogs((prev) => {
                const filtered = prev.filter((current) => current.worklogId !== worklog.worklogId);
                return [
                    ...filtered, worklog
                ];
            });
        });
    }, [setWorklogs]);

    const [
        createDialogOpened, {
            open: createDialogOpen,
            close: createDialogClose
        }
    ] = useDisclosure(false);

    const [createWorklogRange, setCreateWorklogRange] = useState<{
        start: moment.Moment, end: moment.Moment
    }>({
        start: moment(),
        end: moment()
    });

    const onSelectSlot = useCallback((slotInfo: SlotInfo) => {
        setCreateWorklogRange({
            start: moment(slotInfo.start),
            end: moment(slotInfo.end)
        });
        createDialogOpen();
    }, [setCreateWorklogRange, createDialogOpen]);

    const onCreateWorklog = useCallback((worklog: Worklog) => {
        createDialogClose();
        createWorklog(worklog)
        .then(worklog => {
            setWorklogs((prev) => {
                return [
                    ...prev, worklog
                ];
            });
        });
    }, [createDialogClose, setWorklogs]);

    return (
        <>
            <WorklogCalendar worklogs={worklogs}
                onNavigate={onNavigate}
                onWorklogChange={onWorklogChange}
                onSelectSlot={onSelectSlot}/>
            <CreateDialog opened={createDialogOpened}
                onCancel={createDialogClose}
                onCreate={onCreateWorklog}
                range={createWorklogRange}/>
        </>
    );
}

export default App;
