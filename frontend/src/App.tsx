import {useCallback, useEffect, useState} from "react";
import moment from "moment";
import Worklog from "./Worklog";
import WorklogCalendar from "./WorklogCalendar";
import "./App.css";
import {Modal, Text, TextInput} from "@mantine/core";
import {useDisclosure} from "@mantine/hooks";
import {SlotInfo} from "react-big-calendar";
import IssuePicker from "./IssuePicker";

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

    const [selectedWorklog, setSelectedWorklog] = useState<Worklog>();
    const [
        createDialogOpened, {
            open: openCreateDialog,
            close: closeCreateDialog
        }
    ] = useDisclosure(false);

    const onSelectSlot = useCallback((slotInfo: SlotInfo) => {
        setSelectedWorklog({
            start: moment(slotInfo.start).toISOString(),
            end: moment(slotInfo.end).toISOString()
        } as Worklog);
        openCreateDialog();
    }, [setSelectedWorklog, openCreateDialog]);

    const onIssuePicked = useCallback((issueKey: string) => console.log(issueKey), []);

    return (
        <>
            <WorklogCalendar worklogs={worklogs}
                onNavigate={onNavigate}
                onWorklogChange={onWorklogChange}
                onSelectSlot={onSelectSlot}/>
            <Modal opened={createDialogOpened} onClose={closeCreateDialog} title="Create worklog" centered>
                <Text>Start: {moment(selectedWorklog?.start).format("DD.MM.YYYY HH:mm")}</Text>
                <Text>Ende: {moment(selectedWorklog?.end).format("DD.MM.YYYY HH:mm")}</Text>
                <IssuePicker onIssuePicked={onIssuePicked}/>
                <TextInput label="Worklog comment"/>
            </Modal>
        </>
    );
}

export default App;
